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
package de.akquinet.engineering.vaadinator.timesheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import de.akquinet.engineering.vaadinator.timesheet.context.TimesheetContextConstants;
import de.akquinet.engineering.vaadinator.timesheet.model.Timesheet;
import de.akquinet.engineering.vaadinator.timesheet.service.CouchDbProjectService;
import de.akquinet.engineering.vaadinator.timesheet.service.CouchDbTimesheetService;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.presenter.PresenterFactory;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.presenter.PresenterFactoryEx;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.presenter.TimesheetChangePresenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.VaadinViewFactoryEx;

/**
 * Main UI class
 */
@Title("Timesheet")
@Theme("touchkitex")
@Widgetset("de.akquinet.engineering.vaadinator.timesheet.MobileWidgetset")
public class TimesheetUI extends UI implements TimesheetContextConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		// TODO: remove test-entry into contet
		context.put(CONTEXT_LOGIN_USER, "sebastian");
		// create
		NavigationManager m = new NavigationManager();
		m.setMaintainBreadcrumb(true);
		TimesheetChangePresenter pres = obtainPresenterFactory(request.getContextPath()).createTimesheetChangePresenter(null);
		// Load the july timesheet into the presenter
		CouchDbTimesheetService tsService = new CouchDbTimesheetService();
		List<Timesheet> tsList = tsService.listAllTimesheet(new HashMap<String, Object>(context));
		for (Timesheet ts : tsList) {
			if (ts.getMonth() == 7 && ts.getYear() == 2014) {
				pres.setTimesheet(ts);
				break;
			}
		}
		// TODO: have list presenter before (instead of one)
		m.setCurrentComponent((Component) pres.getView().getComponent());
		setContent(m);
		// and go
		pres.startPresenting();
	}

	Map<String, Object> context = new HashMap<String, Object>();
	PresenterFactory presenterFactory = null;

	protected PresenterFactory obtainPresenterFactory(String contextPath) {
		if (presenterFactory == null) {
			// simple, overwrite method for e.g. Spring / CDI / ...
			// Entity-Manager NUR Thread-Safe, wenn er injected wird wie hier
			presenterFactory = new PresenterFactoryEx(context, new VaadinViewFactoryEx(), new CouchDbProjectService(), new CouchDbTimesheetService(), null,
					null);
		}
		return presenterFactory;
	}

}