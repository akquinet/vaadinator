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

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.DisplayPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.FieldType;

@DisplayBean(captionText = "Entry")
public class TimesheetDayEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetDayEntry() {
		super();
	}

	@DisplayProperty(profileSettings = @DisplayPropertySetting(fieldType = FieldType.CUSTOM, customClassName = "com.vaadin.ui.NativeSelect", customAuswahlAusListe = true, required = true))
	private Project project;
	@DisplayProperty(profileSettings = @DisplayPropertySetting(fieldType = FieldType.CUSTOM, customClassName = "de.akquinet.engineering.vaadinator.timesheet.widgetset.TimeField", required = true))
	private String hours;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public float getEffectiveDurationHours() {
		if (getHours() == null) {
			return 0.0f;
		}
		if (!getHours().matches("\\d\\d?:\\d\\d")) {
			// (we handle validation elsewhere)
			return 0.0f;
		}
		String[] hours = getHours().split(":");
		return (Integer.parseInt(hours[0]) * 1.0f) + ((Integer.parseInt(hours[1]) * 1.0f) / 60.0f);
	}

}
