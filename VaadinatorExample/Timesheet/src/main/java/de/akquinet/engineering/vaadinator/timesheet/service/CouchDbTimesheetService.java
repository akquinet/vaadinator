/*
 * Copyright 2014 akquinet engineering GmbH
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.akquinet.engineering.vaadinator.timesheet.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.akquinet.engineering.vaadinator.timesheet.BusinessException;
import de.akquinet.engineering.vaadinator.timesheet.TechnicalException;
import de.akquinet.engineering.vaadinator.timesheet.context.TimesheetContextConstants;
import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.Timesheet;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDay;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;

public class CouchDbTimesheetService extends AbstractCouchDbService implements TimesheetService, TimesheetContextConstants {

	public CouchDbTimesheetService() {
		super();
	}

	@Override
	public List<Timesheet> listAllTimesheet(Map<String, Object> context) {
		String loginUser = (String) context.get(CONTEXT_LOGIN_USER);
		try {
			Pattern idPattern = Pattern.compile("ts_([a-zA-Z0-9]*)_(\\d{4})_(\\d{2})");
			List<Timesheet> res = new ArrayList<Timesheet>();
			JSONObject couchObj = getCouch("_all_docs?startkey=\"ts_"+loginUser+"_\"&endkey=\"ts_"+loginUser+"_zzz\"&include_docs=true");
			JSONArray couchTsList = couchObj.getJSONArray("rows");
			for (int i = 0; i < couchTsList.length(); i++) {
				JSONObject couchTs = couchTsList.getJSONObject(i);
				Timesheet ts = new TimesheetCouch(couchTs.getString("id"), couchTs.getJSONObject("value").getString("rev"));
				String id = couchTs.getString("id");
				Matcher idMatch = idPattern.matcher(id);
				idMatch.find();
				ts.setYear(Integer.parseInt(idMatch.group(2)));
				ts.setMonth(Integer.parseInt(idMatch.group(3)));
				int daysInMonth;
				if (ts.getMonth() == 1 || ts.getMonth() == 3 || ts.getMonth() == 5 || ts.getMonth() == 7 || ts.getMonth() == 8 || ts.getMonth() == 10
						|| ts.getMonth() == 12) {
					daysInMonth = 31;
				} else if (ts.getMonth() == 2 && (ts.getYear() % 4) == 0) {
					daysInMonth = 29;
				} else if (ts.getMonth() == 2 && (ts.getYear() % 4) != 0) {
					daysInMonth = 28;
				} else if (ts.getMonth() == 4 || ts.getMonth() == 6 || ts.getMonth() == 9 || ts.getMonth() == 11) {
					daysInMonth = 30;
				} else {
					throw new IllegalArgumentException("Wrong month in timesheet");
				}
				Map<Integer, TimesheetDay> days = new HashMap<Integer, TimesheetDay>();
				for (int j = 1; j <= daysInMonth; j++) {
					TimesheetDay day = new TimesheetDay();
					day.setDay(j);
					ts.addDay(day);
					days.put(j, day);
				}
				JSONArray couchEntriesList = couchTs.getJSONObject("doc").getJSONArray("entries");
				for (int j = 0; j < couchEntriesList.length(); j++) {
					JSONObject couchEntry = couchEntriesList.getJSONObject(j);
					TimesheetDayEntry dayEntry = new TimesheetDayEntry();
					dayEntry.setHours(couchEntry.getString("hours"));
					Project project = new Project();
					project.setName(couchEntry.getString("project"));
					dayEntry.setProject(project);
					days.get(couchEntry.getInt("dayInMonth")).addEntry(dayEntry);
				}
				res.add(ts);
			}
			return res;
		} catch (JSONException e) {
			throw new TechnicalException(e);
		} catch (MalformedURLException e) {
			throw new TechnicalException(e);
		} catch (IOException e) {
			throw new TechnicalException(e);
		}
	}

	@Override
	public Timesheet addNewTimesheet(Timesheet timesheet, Map<String, Object> context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Timesheet updateExistingTimesheet(Timesheet timesheet, Map<String, Object> context) {
		if (!(timesheet instanceof TimesheetCouch)) {
			throw new IllegalArgumentException("Can only update Timesheets coming from the Couch!");
		}
		try {
			JSONObject couchTs = getCouch(((TimesheetCouch) timesheet).getCouchId());
			if (!(couchTs.getString("_rev").equals(((TimesheetCouch) timesheet).getCouchRev()))) {
				throw new BusinessException("Timesheet was updated by s/o else in the meantime!");
			}
			// update selectively (other info just stays)
			JSONArray couchEntriesList = new JSONArray();
			for (TimesheetDay day : timesheet.getDays()) {
				for (TimesheetDayEntry dayEntry : day.getEntries()) {
					// skip empty for purpose - easiest delete
					if (dayEntry.getProject() == null || Math.abs(dayEntry.getEffectiveDurationHours()) < Float.MIN_NORMAL) {
						continue;
					}
					JSONObject couchEntry = new JSONObject();
					couchEntry.put("dayInMonth", day.getDay());
					couchEntry.put("project", dayEntry.getProject().getName());
					couchEntry.put("hours", dayEntry.getHours());
					couchEntriesList.put(couchEntry);
				}
			}
			couchTs.put("entries", couchEntriesList);
			JSONObject couchPutObj = putCouch(((TimesheetCouch) timesheet).getCouchId(), couchTs);
			if (!couchPutObj.getBoolean("ok")) {
				throw new TechnicalException("Saving failed: " + couchPutObj.toString());
			}
			((TimesheetCouch) timesheet).setCouchRev(couchPutObj.getString("rev"));
			return timesheet;
		} catch (JSONException e) {
			throw new TechnicalException(e);
		} catch (MalformedURLException e) {
			throw new TechnicalException(e);
		} catch (IOException e) {
			throw new TechnicalException(e);
		}
	}

	public static class TimesheetCouch extends Timesheet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TimesheetCouch() {
			super();
		}

		public TimesheetCouch(String couchId, String couchRev) {
			super();
			this.couchId = couchId;
			this.couchRev = couchRev;
		}

		private String couchId;
		private String couchRev;

		public String getCouchId() {
			return couchId;
		}

		public void setCouchId(String couchId) {
			this.couchId = couchId;
		}

		public String getCouchRev() {
			return couchRev;
		}

		public void setCouchRev(String couchRev) {
			this.couchRev = couchRev;
		}

	}

}
