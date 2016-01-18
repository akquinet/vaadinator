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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import de.akquinet.engineering.vaadinator.timesheet.ui.view.ExceptionMappingStrategy;

public class TimesheetChangeViewImplEx extends TimesheetChangeViewImpl implements TimesheetChangeViewEx {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetChangeViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy) {
		super(exceptionMappingStrategy);
	}

	private Map<String, Label> projectSumLabels = new HashMap<String, Label>();

	private static final NumberFormat twoDigitsFormat = NumberFormat.getInstance();
	static {
		twoDigitsFormat.setMaximumFractionDigits(2);
		twoDigitsFormat.setMinimumFractionDigits(2);
		twoDigitsFormat.setMinimumIntegerDigits(1);
	}

	@Override
	public void setProjectSum(String projectName, float sumValue) {
		// add to section basisdaten
		if (!projectSumLabels.containsKey(projectName)) {
			Label newLabel = new Label(twoDigitsFormat.format(0.0));
			newLabel.setCaption("Sum 4 " + projectName);
			sectionBasisdaten.addComponent(newLabel);
			projectSumLabels.put(projectName, newLabel);
		}
		projectSumLabels.get(projectName).setValue(twoDigitsFormat.format(sumValue));
	}

	@Override
	public void addTimesheetDayView(TimesheetDayChangeView view) {
		// this is a little dirty, but we can't compose navigation views, so we
		// stack the layout components
		NavigationView viewCmp = (NavigationView) view.getComponent();
		CssLayout viewLayout = (CssLayout) viewCmp.getContent();
		layout.addComponent(viewLayout);
	}

}
