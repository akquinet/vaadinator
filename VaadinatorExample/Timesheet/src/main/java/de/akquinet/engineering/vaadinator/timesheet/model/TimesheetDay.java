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
package de.akquinet.engineering.vaadinator.timesheet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.DisplayPropertySetting;

@DisplayBean(captionText = "day")
public class TimesheetDay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetDay() {
		super();
	}

	@DisplayProperty(profileSettings = { @DisplayPropertySetting(readOnly = true) })
	private int day;
	private List<TimesheetDayEntry> entries = new ArrayList<TimesheetDayEntry>();

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public List<TimesheetDayEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TimesheetDayEntry> entries) {
		this.entries = entries;
	}

	public void addEntry(TimesheetDayEntry entry) {
		entries.add(entry);
	}

	public void removeEntry(TimesheetDayEntry entry) {
		entries.remove(entry);
	}

}
