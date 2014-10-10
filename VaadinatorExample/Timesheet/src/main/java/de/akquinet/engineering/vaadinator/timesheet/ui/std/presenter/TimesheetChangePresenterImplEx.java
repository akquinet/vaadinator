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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDay;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;
import de.akquinet.engineering.vaadinator.timesheet.service.ProjectService;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetService;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetChangeViewEx;

public class TimesheetChangePresenterImplEx extends TimesheetChangePresenterImpl implements HoursChangeObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetChangePresenterImplEx(Map<String, Object> context, PresenterFactoryEx presenterFactory, TimesheetChangeViewEx view, Presenter returnPresenter,
			TimesheetService service, ProjectService projectService) {
		super(context, view, returnPresenter, service);
		this.context = context;
		this.presenterFactory = presenterFactory;
		this.projectService = projectService;
	}

	public TimesheetChangePresenterImplEx(Map<String, Object> context, PresenterFactoryEx presenterFactory, TimesheetChangeViewEx view, Presenter returnPresenter,
			SubviewCapablePresenter capablePresenter, TimesheetService service, ProjectService projectService) {
		super(context, view, returnPresenter, capablePresenter, service);
		this.context = context;
		this.presenterFactory = presenterFactory;
		this.projectService = projectService;
	}

	private Map<String, Object> context;
	private PresenterFactoryEx presenterFactory;
	private ProjectService projectService;

	List<TimesheetDayChangePresenterEx> dayPresenters = new ArrayList<TimesheetDayChangePresenterEx>();

	@Override
	public TimesheetChangeViewEx getView() {
		return (TimesheetChangeViewEx) super.getView();
	}

	@Override
	public void startPresenting() {
		super.startPresenting();
		// obtain the choice of Projects (once)
		List<Project> projectList = new ArrayList<Project>();
		projectList.addAll(projectService.listAllProject(new HashMap<String, Object>(context)));
		// initialize sums for sorted project List
		Collections.sort(projectList, new Comparator<Project>() {

			@Override
			public int compare(Project o1, Project o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		// cascade
		dayPresenters.clear();
		for (TimesheetDay day : getTimesheet().getDays()) {
			TimesheetDayChangePresenterEx dayPresenter = presenterFactory.createTimesheetDayChangePresenter(null);
			dayPresenter.setTimesheetDay(day);
			dayPresenter.setProjectList(projectList);
			dayPresenter.setHoursChangeObserver(this);
			dayPresenters.add(dayPresenter);
			dayPresenter.startPresenting();
			getView().addTimesheetDayView(dayPresenter.getView());
		}
		// set our sums initially
		for (Project project : projectList) {
			calculateAndSetProjectSum(project);
		}
	}

	@Override
	protected void loadFromModel() {
		super.loadFromModel();
		// cascade
		for (TimesheetDayChangePresenterEx dayPresenter : dayPresenters) {
			dayPresenter.loadFromModel();
		}
	}

	@Override
	protected void saveToModel() {
		super.saveToModel();
		// cascade
		for (TimesheetDayChangePresenterEx dayPresenter : dayPresenters) {
			dayPresenter.saveToModel();
		}
	}

	@Override
	public void onSave() {
		boolean valid = true;
		valid = valid && getView().checkAllFieldsValid();
		for (TimesheetDayChangePresenterEx dayPresenter : dayPresenters) {
			valid = valid && dayPresenter.validateFields();
		}
		if (!valid) {
			return;
		}
		super.onSave();
	}

	@Override
	public void onRemove() {
		// timesheets can not be removed
	}

	@Override
	public void hoursChanged(Project project) {
		calculateAndSetProjectSum(project);
	}

	private void calculateAndSetProjectSum(Project project) {
		float sum = 0.0f;
		for (TimesheetDay day : getTimesheet().getDays()) {
			for (TimesheetDayEntry entry : day.getEntries()) {
				if (entry.getProject() != null && entry.getProject().equals(project)) {
					sum += entry.getEffectiveDurationHours();
				}
			}
		}
		getView().setProjectSum(project.getName(), sum);
	}

}
