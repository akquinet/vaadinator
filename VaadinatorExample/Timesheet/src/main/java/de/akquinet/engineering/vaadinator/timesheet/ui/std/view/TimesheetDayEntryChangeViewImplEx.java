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
package de.akquinet.engineering.vaadinator.timesheet.ui.std.view;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.ui.view.ButtonFactory;
import de.akquinet.engineering.vaadinator.timesheet.ui.view.ExceptionMappingStrategy;

public class TimesheetDayEntryChangeViewImplEx extends TimesheetDayEntryChangeViewImpl implements TimesheetDayEntryChangeViewEx {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetDayEntryChangeViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy,
			ButtonFactory buttonFactory) {
		super(exceptionMappingStrategy, buttonFactory);
	}

	private TimesheetDayEntryChangeViewEx.Observer observer = null;

	@Override
	public void initializeUi() {
		super.initializeUi();
		// don't show a section title
		sectionBasisdaten.setCaption(null);
		// null projects: remove item
		project.setNullSelectionAllowed(true);
		// react on project change
		project.setImmediate(true);
		project.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (observer != null) {
					observer.onProjectChanged();
				}
			}
		});
		// react immediately on hours change
		hours.setImmediate(true);
		hours.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (observer != null) {
					observer.onHoursChanged();
				}
			}
		});
	}

	@Override
	protected String obtainCaptionForProject(Project auswahl) {
		return auswahl.getName();
	}

	@Override
	public void setObserver(de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayEntryChangeView.Observer observer) {
		super.setObserver(observer);
		if (observer instanceof TimesheetDayEntryChangeViewEx.Observer) {
			this.observer = (TimesheetDayEntryChangeViewEx.Observer) observer;
		}
	}

	@Override
	public void setHours(String hours) {
		if (hours != null && hours.length() == 4) {
			hours = "0" + hours;
		}
		super.setHours(hours);
	}

}
