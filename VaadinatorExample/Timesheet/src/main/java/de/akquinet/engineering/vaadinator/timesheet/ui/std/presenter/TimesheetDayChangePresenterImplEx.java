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
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetDayService;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayChangeViewEx;

public class TimesheetDayChangePresenterImplEx extends TimesheetDayChangePresenterImpl implements TimesheetDayChangePresenterEx,
		TimesheetDayChangeViewEx.Observer, HoursChangeObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimesheetDayChangePresenterImplEx(Map<String, Object> context, PresenterFactoryEx presenterFactory, TimesheetDayChangeViewEx view, Presenter returnPresenter,
			TimesheetDayService service) {
		super(context, view, returnPresenter, service);
		this.presenterFactory = presenterFactory;
	}

	public TimesheetDayChangePresenterImplEx(Map<String, Object> context, PresenterFactoryEx presenterFactory, TimesheetDayChangeViewEx view, Presenter returnPresenter,
			SubviewCapablePresenter capablePresenter, TimesheetDayService service) {
		super(context, view, returnPresenter, capablePresenter, service);
		this.presenterFactory = presenterFactory;
	}

	private PresenterFactoryEx presenterFactory;

	private List<Project> projectList = new ArrayList<Project>();
	
	private HoursChangeObserver hoursChangeObserver = null;

	List<TimesheetDayEntryChangePresenterEx> entryPresenters = new ArrayList<TimesheetDayEntryChangePresenterEx>();

	@Override
	public TimesheetDayChangeViewEx getView() {
		return (TimesheetDayChangeViewEx) super.getView();
	}

	@Override
	public void startPresenting() {
		super.startPresenting();
		// cascade
		entryPresenters.clear();
		for (TimesheetDayEntry entry : getTimesheetDay().getEntries()) {
			TimesheetDayEntryChangePresenterEx entryPresenter = presenterFactory.createTimesheetDayEntryChangePresenter(null);
			entryPresenter.setTimesheetDayEntry(entry);
			entryPresenter.setProjectList(getProjectList());
			entryPresenter.setHoursChangeObserver(this);
			entryPresenters.add(entryPresenter);
			entryPresenter.startPresenting();
			getView().addTimesheetDayEntryView(entryPresenter.getView());
		}
	}

	@Override
	public void loadFromModel() {
		super.loadFromModel();
		// cascade
		for (TimesheetDayEntryChangePresenterEx entryPresenter : entryPresenters) {
			entryPresenter.loadFromModel();
		}
	}

	@Override
	public boolean validateFields() {
		boolean valid = true;
		valid = valid && getView().checkAllFieldsValid();
		for (TimesheetDayEntryChangePresenterEx entryPresenter : entryPresenters) {
			valid = valid && entryPresenter.validateFields();
		}
		return valid;
	}

	@Override
	public void saveToModel() {
		super.saveToModel();
		// cascade
		for (TimesheetDayEntryChangePresenterEx entryPresenter : entryPresenters) {
			entryPresenter.saveToModel();
		}
	}

	@Override
	public void onAdditionalEntry() {
		TimesheetDayEntry newEntry = new TimesheetDayEntry();
		getTimesheetDay().addEntry(newEntry);
		TimesheetDayEntryChangePresenterEx entryPresenter = presenterFactory.createTimesheetDayEntryChangePresenter(null);
		entryPresenter.setTimesheetDayEntry(newEntry);
		entryPresenter.setProjectList(getProjectList());
		entryPresenter.setHoursChangeObserver(this);
		entryPresenters.add(entryPresenter);
		entryPresenter.startPresenting();
		getView().addTimesheetDayEntryView(entryPresenter.getView());
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
		// it is pointless to remove days
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

	@Override
	public void hoursChanged(Project project) {
		// just cascade up
		if (hoursChangeObserver != null) {
			hoursChangeObserver.hoursChanged(project);
		}
	}

}
