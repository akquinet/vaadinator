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
package de.akquinet.engineering.vaadinator.timesheet.widgetset.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(de.akquinet.engineering.vaadinator.timesheet.widgetset.TimeField.class)
public class TimeFieldConnector extends TextFieldConnector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Widget createWidget() {
		return GWT.create(TimeFieldWidget.class);
	}

	@Override
	public TimeFieldWidget getWidget() {
		return (TimeFieldWidget) super.getWidget();
	}

}