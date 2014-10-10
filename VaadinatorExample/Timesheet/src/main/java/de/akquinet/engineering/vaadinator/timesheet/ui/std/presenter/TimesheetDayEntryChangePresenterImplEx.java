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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetDayEntryService;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayEntryChangeView;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayEntryChangeViewEx;

public class TimesheetDayEntryChangePresenterImplEx extends TimesheetDayEntryChangePresenterImpl implements TimesheetDayEntryChangePresenterEx,
		TimesheetDayEntryChangeViewEx.Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Project> projectList = new ArrayList<Project>();
	// remember the "old" project for changes
	Project oldProject = null;
	// don't fire events before we're fully operational
	boolean presenting = false;

	private HoursChangeObserver hoursChangeObserver = null;

	public TimesheetDayEntryChangePresenterImplEx(Map<String, Object> context, TimesheetDayEntryChangeViewEx view, Presenter returnPresenter, TimesheetDayEntryService service) {
		super(context, view, returnPresenter, service);
	}

	public TimesheetDayEntryChangePresenterImplEx(Map<String, Object> context, TimesheetDayEntryChangeView view, Presenter returnPresenter,
			SubviewCapablePresenter capablePresenter, TimesheetDayEntryService service) {
		super(context, view, returnPresenter, capablePresenter, service);
	}

	@Override
	public TimesheetDayEntryChangeViewEx getView() {
		return (TimesheetDayEntryChangeViewEx) super.getView();
	}

	@Override
	public void startPresenting() {
		// set project choices
		getView().setChoicesForProject(getProjectList());
		// then set values, etc.
		super.startPresenting();
		// now fire events
		presenting = true;
	}

	@Override
	public void loadFromModel() {
		oldProject = getTimesheetDayEntry() == null ? null : getTimesheetDayEntry().getProject();
		// null means 00:00 hours worked
		if (getTimesheetDayEntry() != null && getTimesheetDayEntry().getHours() == null) {
			getTimesheetDayEntry().setHours("00:00");
		}
		super.loadFromModel();
	}

	@Override
	public boolean validateFields() {
		return getView().checkAllFieldsValid();
	}

	@Override
	public void saveToModel() {
		super.saveToModel();
	}

	@Override
	public void onProjectChanged() {
		if (!presenting) {
			return;
		}
		saveToModel();
		if (hoursChangeObserver != null) {
			if (oldProject != null) {
				hoursChangeObserver.hoursChanged(oldProject);
			}
			oldProject = getTimesheetDayEntry().getProject();
			if (getTimesheetDayEntry().getProject() != null) {
				hoursChangeObserver.hoursChanged(getTimesheetDayEntry().getProject());
			}
		}
	}

	@Override
	public void onHoursChanged() {
		if (!presenting) {
			return;
		}
		saveToModel();
		if (hoursChangeObserver != null) {
			if (getTimesheetDayEntry().getProject() != null) {
				hoursChangeObserver.hoursChanged(getTimesheetDayEntry().getProject());
			}
		}
	}

	@Override
	public void onSave() {
		// no action, we cascade
	}

	@Override
	public void onCancel() {
		// no action, we cascade
	}

	@Override
	public void onRemove() {
		// will be done by project == null
	}

	@Override
	public List<Project> getProjectList() {
		return projectList;
	}

	@Override
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	@Override
	public void setHoursChangeObserver(HoursChangeObserver observer) {
		this.hoursChangeObserver = observer;
	}

}
