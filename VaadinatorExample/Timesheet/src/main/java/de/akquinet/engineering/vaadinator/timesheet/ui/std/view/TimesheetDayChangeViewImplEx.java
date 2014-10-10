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

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;

public class TimesheetDayChangeViewImplEx extends TimesheetDayChangeViewImpl implements TimesheetDayChangeViewEx {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetDayChangeViewImplEx() {
		super();
	}

	private int day = 0;

	private TimesheetDayChangeViewEx.Observer observer = null;

	protected Button plusEntry = new Button("+");

	@Override
	public void initializeUi() {
		super.initializeUi();
		// change default behavior: store the day in the section caption (and
		// hide the day field)
		super.day.setVisible(false);
		setSectionCaption(day);
		plusEntry.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				observer.onAdditionalEntry();
			}
		});
		layout.addComponent(plusEntry);
	}

	@Override
	public int getDay() {
		return day;
	}

	@Override
	public void setDay(int day) {
		this.day = day;
		setSectionCaption(day);
	}

	private void setSectionCaption(int day) {
		sectionBasisdaten.setCaption("Tag " + day);
	}

	@Override
	public void addTimesheetDayEntryView(TimesheetDayEntryChangeView view) {
		// this is a little dirty, but we can't compose navigation views, so we
		// stack the layout components
		NavigationView viewCmp = (NavigationView) view.getComponent();
		CssLayout viewLayout = (CssLayout) viewCmp.getContent();
		layout.addComponent(viewLayout);
		// re-add plus at the end
		layout.removeComponent(plusEntry);
		layout.addComponent(plusEntry);
	}

	@Override
	public void setObserver(de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayChangeView.Observer observer) {
		super.setObserver(observer);
		if (observer instanceof TimesheetDayChangeViewEx.Observer) {
			this.observer = (TimesheetDayChangeViewEx.Observer) observer;
		}
	}

}
