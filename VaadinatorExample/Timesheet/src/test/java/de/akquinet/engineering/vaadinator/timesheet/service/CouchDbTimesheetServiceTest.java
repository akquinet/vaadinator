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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.timesheet.BusinessException;
import de.akquinet.engineering.vaadinator.timesheet.TechnicalException;
import de.akquinet.engineering.vaadinator.timesheet.context.TimesheetContextConstants;
import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.Timesheet;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDay;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;
import de.akquinet.engineering.vaadinator.timesheet.service.CouchDbTimesheetService.TimesheetCouch;

public class CouchDbTimesheetServiceTest {

	Map<String, String> getResults = new HashMap<String, String>();
	Map<String, String> putResults = new HashMap<String, String>();
	List<String> getUrls = new ArrayList<String>();
	List<String> putUrls = new ArrayList<String>();
	List<String> putContents = new ArrayList<String>();

	Map<String, Object> context = new HashMap<String, Object>();
	CouchDbTimesheetService timesheetService;

	@Before
	public void setUp() {
		context.put(TimesheetContextConstants.CONTEXT_LOGIN_USER, "sebastian");
		timesheetService = new CouchDbTimesheetService() {

			@Override
			protected JSONObject getCouch(String urlPart) throws IOException, MalformedURLException, JSONException {
				getUrls.add(urlPart);
				return getResults.containsKey(urlPart) ? (new JSONObject(getResults.get(urlPart))) : null;
			}

			@Override
			protected JSONObject putCouch(String urlPart, JSONObject content) throws IOException, MalformedURLException, JSONException {
				putUrls.add(urlPart);
				putContents.add(content.toString());
				return putResults.containsKey(urlPart) ? (new JSONObject(putResults.get(urlPart))) : null;
			}

		};
	}

	@Test
	public void testListAllTimesheet() {
		getResults
				.put("_all_docs?startkey=\"ts_sebastian_\"&endkey=\"ts_sebastian_zzz\"&include_docs=true",
						"{\"total_rows\":9,\"offset\":8,\"rows\":[{\"id\":\"ts_sebastian_2014_07\",\"key\":\"ts_sebastian_2014_07\",\"value\":{\"rev\":\"2-1983e046a14377c433c08711fbb57b28\"},\"doc\":{\"_id\":\"ts_sebastian_2014_07\",\"_rev\":\"2-1983e046a14377c433c08711fbb57b28\",\"entries\":[{\"dayInMonth\":1,\"project\":\"P1\",\"hours\":\"2:31\"},{\"dayInMonth\":2,\"project\":\"P1\",\"hours\":\"1:11\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"2:22\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"3:33\"},{\"dayInMonth\":14,\"project\":\"P1\",\"hours\":\"1:00\"}]}}]}");
		List<Timesheet> res = timesheetService.listAllTimesheet(context);
		assertEquals(1, getUrls.size());
		assertEquals(0, putUrls.size());
		assertEquals(1, res.size());
		Timesheet ts = res.get(0);
		assertNotNull(ts);
		assertEquals(TimesheetCouch.class, ts.getClass());
		assertEquals("ts_sebastian_2014_07", ((TimesheetCouch) ts).getCouchId());
		assertEquals("2-1983e046a14377c433c08711fbb57b28", ((TimesheetCouch) ts).getCouchRev());
		assertEquals(7, ts.getMonth());
		assertEquals(2014, ts.getYear());
		assertEquals(31, ts.getDays().size());
		assertEquals(1, ts.getDays().get(0).getDay());
		assertEquals(1, ts.getDays().get(0).getEntries().size());
		assertEquals("P1", ts.getDays().get(0).getEntries().get(0).getProject().getName());
		assertEquals("2:31", ts.getDays().get(0).getEntries().get(0).getHours());
		assertEquals(2, ts.getDays().get(1).getDay());
		assertEquals(3, ts.getDays().get(1).getEntries().size());
		assertEquals(14, ts.getDays().get(13).getDay());
		assertEquals(1, ts.getDays().get(13).getEntries().size());
		assertEquals(15, ts.getDays().get(14).getDay());
		assertEquals(0, ts.getDays().get(14).getEntries().size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddnewTimesheet() {
		timesheetService.addNewTimesheet(new Timesheet(), context);
	}

	@Test
	public void testUpdateExistingTimesheet() throws JSONException {
		getResults
				.put("ts_sebastian_2014_07",
						"{\"_id\":\"ts_sebastian_2014_07\",\"_rev\":\"2-1983e046a14377c433c08711fbb57b28\", \"other\": 88,\"entries\":[{\"dayInMonth\":1,\"project\":\"P1\",\"hours\":\"2:31\"},{\"dayInMonth\":2,\"project\":\"P1\",\"hours\":\"1:11\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"2:22\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"3:33\"},{\"dayInMonth\":14,\"project\":\"P1\",\"hours\":\"1:00\"}]}");
		putResults.put("ts_sebastian_2014_07", "{\"ok\": true, \"id\": \"ts_sebastian_2014_07\", \"rev\": \"3-123456789\"}");
		TimesheetCouch ts = new TimesheetCouch("ts_sebastian_2014_07", "2-1983e046a14377c433c08711fbb57b28");
		// add some test data in that order
		{
			TimesheetDay emptyDay = new TimesheetDay();
			emptyDay.setDay(2);
			ts.addDay(emptyDay);
		}
		{
			TimesheetDay dayWithOne = new TimesheetDay();
			dayWithOne.setDay(3);
			{
				TimesheetDayEntry normalEntry = new TimesheetDayEntry();
				normalEntry.setProject(new Project("bla"));
				normalEntry.setHours("1:30");
				dayWithOne.addEntry(normalEntry);
			}
			ts.addDay(dayWithOne);
		}
		{
			TimesheetDay dayWithSeveral = new TimesheetDay();
			dayWithSeveral.setDay(14);
			{
				TimesheetDayEntry oneMinuteEntry = new TimesheetDayEntry();
				oneMinuteEntry.setProject(new Project("bla"));
				oneMinuteEntry.setHours("00:01");
				dayWithSeveral.addEntry(oneMinuteEntry);
			}
			{
				TimesheetDayEntry oneHourEntry = new TimesheetDayEntry();
				oneHourEntry.setProject(new Project("blup"));
				oneHourEntry.setHours("01:00");
				dayWithSeveral.addEntry(oneHourEntry);
			}
			{
				TimesheetDayEntry noProjectEntry = new TimesheetDayEntry();
				noProjectEntry.setProject(null);
				noProjectEntry.setHours("01:00");
				dayWithSeveral.addEntry(noProjectEntry);
			}
			{
				TimesheetDayEntry noTimeEntry = new TimesheetDayEntry();
				noTimeEntry.setProject(new Project("blup"));
				noTimeEntry.setHours(null);
				dayWithSeveral.addEntry(noTimeEntry);
			}
			{
				TimesheetDayEntry zeroTimeEntry = new TimesheetDayEntry();
				zeroTimeEntry.setProject(new Project("blup"));
				zeroTimeEntry.setHours("0:00");
				dayWithSeveral.addEntry(zeroTimeEntry);
			}
			ts.addDay(dayWithSeveral);
		}
		Timesheet res = timesheetService.updateExistingTimesheet(ts, context);
		assertEquals(1, getUrls.size());
		assertEquals(1, putUrls.size());
		assertEquals(1, putContents.size());
		assertEquals(TimesheetCouch.class, res.getClass());
		assertEquals("ts_sebastian_2014_07", ((TimesheetCouch) res).getCouchId());
		assertEquals("3-123456789", ((TimesheetCouch) res).getCouchRev());
		// validate what is in the data sent:
		JSONObject sentObj = new JSONObject(putContents.get(0));
		assertEquals("ts_sebastian_2014_07", sentObj.getString("_id"));
		assertEquals("2-1983e046a14377c433c08711fbb57b28", sentObj.getString("_rev"));
		assertEquals(88, sentObj.getInt("other"));
		JSONArray sentEntries = sentObj.getJSONArray("entries");
		assertNotNull(sentEntries);
		assertEquals(3, sentEntries.length());
		assertEquals(3, sentEntries.getJSONObject(0).getInt("dayInMonth"));
		assertEquals("bla", sentEntries.getJSONObject(0).getString("project"));
		assertEquals("1:30", sentEntries.getJSONObject(0).getString("hours"));
		assertEquals(14, sentEntries.getJSONObject(1).getInt("dayInMonth"));
		assertEquals("bla", sentEntries.getJSONObject(1).getString("project"));
		assertEquals("00:01", sentEntries.getJSONObject(1).getString("hours"));
		assertEquals(14, sentEntries.getJSONObject(2).getInt("dayInMonth"));
		assertEquals("blup", sentEntries.getJSONObject(2).getString("project"));
		assertEquals("01:00", sentEntries.getJSONObject(2).getString("hours"));
	}

	@Test
	public void testUpdateExistingTimesheetWrongResponse() {
		getResults
				.put("ts_sebastian_2014_07",
						"{\"_id\":\"ts_sebastian_2014_07\",\"_rev\":\"2-1983e046a14377c433c08711fbb57b28\", \"other\": 88,\"entries\":[{\"dayInMonth\":1,\"project\":\"P1\",\"hours\":\"2:31\"},{\"dayInMonth\":2,\"project\":\"P1\",\"hours\":\"1:11\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"2:22\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"3:33\"},{\"dayInMonth\":14,\"project\":\"P1\",\"hours\":\"1:00\"}]}");
		putResults.put("ts_sebastian_2014_07", "{\"ok\": false, \"id\": \"ts_sebastian_2014_07\", \"rev\": \"3-123456789\"}");
		TimesheetCouch ts = new TimesheetCouch("ts_sebastian_2014_07", "2-1983e046a14377c433c08711fbb57b28");
		Exception exc = null;
		try {
			timesheetService.updateExistingTimesheet(ts, context);
		} catch (Exception e) {
			exc = e;
		}
		assertNotNull(exc);
		assertEquals(TechnicalException.class, exc.getClass());
		assertTrue(exc.getMessage().contains("Saving failed"));
		assertTrue(exc.getMessage().contains("\"ok\":false"));
	}

	@Test
	public void testUpdateExistingTimesheetWrongRevision() {
		Exception exc = null;
		try {
			getResults
					.put("ts_sebastian_2014_07",
							"{\"_id\":\"ts_sebastian_2014_07\",\"_rev\":\"2-1983e046a14377c433c08711fbb57b28\",\"entries\":[{\"dayInMonth\":1,\"project\":\"P1\",\"hours\":\"2:31\"},{\"dayInMonth\":2,\"project\":\"P1\",\"hours\":\"1:11\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"2:22\"},{\"dayInMonth\":2,\"project\":\"P2\",\"hours\":\"3:33\"},{\"dayInMonth\":14,\"project\":\"P1\",\"hours\":\"1:00\"}]}");
			TimesheetCouch ts = new TimesheetCouch("ts_sebastian_2014_07", "1-abcdef");
			timesheetService.updateExistingTimesheet(ts, context);
		} catch (Exception e) {
			exc = e;
		}
		assertNotNull(exc);
		assertEquals(BusinessException.class, exc.getClass());
		assertEquals("Timesheet was updated by s/o else in the meantime!", exc.getMessage());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateExistingTimesheetNoCouch() {
		timesheetService.updateExistingTimesheet(new Timesheet(), context);
	}

}
