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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.floatThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.Timesheet;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDay;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;
import de.akquinet.engineering.vaadinator.timesheet.service.ProjectService;
import de.akquinet.engineering.vaadinator.timesheet.service.TimesheetService;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetChangeViewEx;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayChangeView;

public class TimesheetChangePresenterImplExTest {

	TimesheetChangeViewEx view;
	TimesheetService service;
	ProjectService projectService;
	TimesheetDayChangePresenterEx dayChangePresenter;
	PresenterFactoryEx presenterFactory;

	Project project;
	Timesheet timesheet;

	TimesheetChangePresenterImplEx presenterImpl;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		view = mock(TimesheetChangeViewEx.class);
		service = mock(TimesheetService.class);
		projectService = mock(ProjectService.class);
		project = new Project("test");
		when(projectService.listAllProject(anyMap())).thenReturn(Collections.singletonList(project));
		dayChangePresenter = mock(TimesheetDayChangePresenterEx.class);
		when(dayChangePresenter.getView()).thenReturn(mock(TimesheetDayChangeView.class));
		presenterFactory = mock(PresenterFactoryEx.class);
		when(presenterFactory.createTimesheetDayChangePresenter(any(Presenter.class))).thenReturn(dayChangePresenter);
		timesheet = new Timesheet();
		timesheet.setYear(2014);
		timesheet.setMonth(7);
		{
			TimesheetDay day = new TimesheetDay();
			day.setDay(1);
			timesheet.addDay(day);
		}
		{
			TimesheetDay day = new TimesheetDay();
			day.setDay(2);
			{
				TimesheetDayEntry entry = new TimesheetDayEntry();
				entry.setHours("1:22");
				entry.setProject(project);
				day.addEntry(entry);
			}
			{
				TimesheetDayEntry entry = new TimesheetDayEntry();
				entry.setHours("2:33");
				entry.setProject(null);
				day.addEntry(entry);
			}
			{
				TimesheetDayEntry entry = new TimesheetDayEntry();
				entry.setHours(null);
				entry.setProject(project);
				day.addEntry(entry);
			}
			timesheet.addDay(day);
		}
		presenterImpl = new TimesheetChangePresenterImplEx(new HashMap<String, Object>(), presenterFactory, view, null, null, service, projectService);
		presenterImpl.setTimesheet(timesheet);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testStartPresenting() {
		presenterImpl.startPresenting();
		verify(view).setObserver(presenterImpl);
		verify(view).initializeUi();
		verify(view).setYear(2014);
		verify(view).setMonth(7);
		verify(projectService).listAllProject(anyMap());
		verify(presenterFactory, times(2)).createTimesheetDayChangePresenter(null);
		verify(dayChangePresenter, times(2)).setTimesheetDay(any(TimesheetDay.class));
		verify(dayChangePresenter, times(2)).setProjectList(anyListOf(Project.class));
		verify(dayChangePresenter, times(2)).setHoursChangeObserver(presenterImpl);
		verify(dayChangePresenter, times(2)).startPresenting();
		verify(view, times(2)).addTimesheetDayView(any(TimesheetDayChangeView.class));
		// need a low delta in here
		verify(view, times(1)).setProjectSum(eq("test"), floatThat(new BaseMatcher<Float>() {

			float target = 1.0f + 22.0f / 60.0f;

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("Roundabout " + target);
			}

			@Override
			public boolean matches(Object arg0) {
				return Math.abs(((Float) arg0).floatValue() - target) < Float.MIN_NORMAL;
			}
		}));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnSave() {
		List<TimesheetDayChangePresenterEx> dayPresenters = new ArrayList<TimesheetDayChangePresenterEx>();
		dayPresenters.add(dayChangePresenter);
		dayPresenters.add(dayChangePresenter);
		presenterImpl.dayPresenters = dayPresenters;
		when(view.checkAllFieldsValid()).thenReturn(true);
		when(dayChangePresenter.validateFields()).thenReturn(true);
		// prep done, test!
		presenterImpl.onSave();
		verify(view, atLeast(1)).checkAllFieldsValid();
		verify(dayChangePresenter, times(2)).validateFields();
		verify(dayChangePresenter, times(2)).saveToModel();
		verify(service).updateExistingTimesheet(eq(timesheet), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnSaveNotValid() {
		List<TimesheetDayChangePresenterEx> dayPresenters = new ArrayList<TimesheetDayChangePresenterEx>();
		dayPresenters.add(dayChangePresenter);
		dayPresenters.add(dayChangePresenter);
		presenterImpl.dayPresenters = dayPresenters;
		when(view.checkAllFieldsValid()).thenReturn(true);
		when(dayChangePresenter.validateFields()).thenReturn(false);
		// prep done, test!
		presenterImpl.onSave();
		verify(view).checkAllFieldsValid();
		verify(dayChangePresenter, times(1)).validateFields();
		verify(dayChangePresenter, never()).saveToModel();
		verify(service, never()).updateExistingTimesheet(any(Timesheet.class), anyMap());
	}

	@Test
	public void testHoursChanged() {
		presenterImpl.hoursChanged(project);
		// need a low delta in here
		verify(view, times(1)).setProjectSum(eq("test"), floatThat(new BaseMatcher<Float>() {

			float target = 1.0f + 22.0f / 60.0f;

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("Roundabout " + target);
			}

			@Override
			public boolean matches(Object arg0) {
				return Math.abs(((Float) arg0).floatValue() - target) < Float.MIN_NORMAL;
			}
		}));
	}

}
