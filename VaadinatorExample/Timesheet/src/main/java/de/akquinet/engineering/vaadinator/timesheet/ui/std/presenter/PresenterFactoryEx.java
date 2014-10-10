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
package de.akquinet.engineering.vaadinator.timesheet.ui.std.presenter;

import java.util.Map;

import de.akquinet.engineering.vaadinator.timesheet.service.ProjectService;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetDayEntryService;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetDayService;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetService;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.ViewFactoryEx;

public class PresenterFactoryEx extends PresenterFactory {

	public PresenterFactoryEx(Map<String, Object> context,ViewFactoryEx viewFactory, ProjectService projectService, TimesheetService timesheetService,
			TimesheetDayService timesheetDayService, TimesheetDayEntryService timesheetDayEntryService) {
		super(context, viewFactory, projectService, timesheetService, timesheetDayService, timesheetDayEntryService);
		this.context = context;
		this.viewFactory = viewFactory;
		this.projectService = projectService;
		this.timesheetService = timesheetService;
	}

	private Map<String, Object> context;
	private ViewFactoryEx viewFactory;
	private ProjectService projectService;
	private TimesheetService timesheetService;

	@Override
	public TimesheetChangePresenter createTimesheetChangePresenter(Presenter returnPresenter) {
		return new TimesheetChangePresenterImplEx(context, this, viewFactory.createTimesheetChangeView(), returnPresenter, timesheetService, projectService);
	}

	@Override
	public TimesheetDayChangePresenterEx createTimesheetDayChangePresenter(Presenter returnPresenter) {
		return new TimesheetDayChangePresenterImplEx(context, this, viewFactory.createTimesheetDayChangeView(), returnPresenter, null);
	}

	@Override
	public TimesheetDayEntryChangePresenterEx createTimesheetDayEntryChangePresenter(Presenter returnPresenter) {
		return new TimesheetDayEntryChangePresenterImplEx(context, viewFactory.createTimesheetDayEntryChangeView(), returnPresenter, null);
	}

}
