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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDay;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;
import de.akquinet.engineering.vaadinator.timesheet.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayChangeViewEx;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayEntryChangeView;

public class TimesheetDayChangePresenterImplExTest {

	TimesheetDayChangeViewEx view;
	HoursChangeObserver observer;
	TimesheetDayEntryChangePresenterEx entryChangePresenter;
	PresenterFactoryEx presenterFactory;

	TimesheetDay timesheetDay;
	Project project;

	TimesheetDayChangePresenterImplEx presenterImpl;

	@Before
	public void setUp() {
		view = mock(TimesheetDayChangeViewEx.class);
		observer = mock(HoursChangeObserver.class);
		entryChangePresenter = mock(TimesheetDayEntryChangePresenterEx.class);
		when(entryChangePresenter.getView()).thenReturn(mock(TimesheetDayEntryChangeView.class));
		presenterFactory = mock(PresenterFactoryEx.class);
		when(presenterFactory.createTimesheetDayEntryChangePresenter(any(Presenter.class))).thenReturn(entryChangePresenter);
		timesheetDay = new TimesheetDay();
		timesheetDay.setDay(3);
		{
			TimesheetDayEntry entry = new TimesheetDayEntry();
			entry.setHours("1:22");
			entry.setProject(project);
			timesheetDay.addEntry(entry);
		}
		{
			TimesheetDayEntry entry = new TimesheetDayEntry();
			entry.setHours("2:33");
			entry.setProject(null);
			timesheetDay.addEntry(entry);
		}
		{
			TimesheetDayEntry entry = new TimesheetDayEntry();
			entry.setHours(null);
			entry.setProject(project);
			timesheetDay.addEntry(entry);
		}
		project = new Project("test");
		presenterImpl = new TimesheetDayChangePresenterImplEx(new HashMap<String, Object>(), presenterFactory, view, null, null, null);
		presenterImpl.setHoursChangeObserver(observer);
		presenterImpl.setTimesheetDay(timesheetDay);
		presenterImpl.setProjectList(Collections.singletonList(project));
	}

	@Test
	public void testStartPresenting() {
		presenterImpl.startPresenting();
		verify(view).setObserver(presenterImpl);
		verify(view).initializeUi();
		verify(view).setDay(3);
		verify(presenterFactory, times(3)).createTimesheetDayEntryChangePresenter(null);
		verify(entryChangePresenter, times(3)).setTimesheetDayEntry(any(TimesheetDayEntry.class));
		verify(entryChangePresenter, times(3)).setProjectList(anyListOf(Project.class));
		verify(entryChangePresenter, times(3)).setHoursChangeObserver(presenterImpl);
		verify(entryChangePresenter, times(3)).startPresenting();
		verify(view, times(3)).addTimesheetDayEntryView(any(TimesheetDayEntryChangeView.class));
	}

	@Test
	public void testLoadFromModel() {
		List<TimesheetDayEntryChangePresenterEx> entryPresenters = new ArrayList<TimesheetDayEntryChangePresenterEx>();
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		presenterImpl.entryPresenters = entryPresenters;
		// prep done, test!
		presenterImpl.loadFromModel();
		verify(view).setDay(3);
		verify(entryChangePresenter, times(3)).loadFromModel();
	}

	@Test
	public void testValidateFields() {
		List<TimesheetDayEntryChangePresenterEx> entryPresenters = new ArrayList<TimesheetDayEntryChangePresenterEx>();
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		presenterImpl.entryPresenters = entryPresenters;
		when(view.checkAllFieldsValid()).thenReturn(true);
		when(entryChangePresenter.validateFields()).thenReturn(true);
		// prep done, test!
		boolean res = presenterImpl.validateFields();
		assertTrue(res);
		verify(view).checkAllFieldsValid();
		verify(entryChangePresenter, times(3)).validateFields();
	}

	@Test
	public void testValidateFieldsNotValid() {
		List<TimesheetDayEntryChangePresenterEx> entryPresenters = new ArrayList<TimesheetDayEntryChangePresenterEx>();
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		presenterImpl.entryPresenters = entryPresenters;
		when(view.checkAllFieldsValid()).thenReturn(true);
		when(entryChangePresenter.validateFields()).thenReturn(false);
		// prep done, test!
		boolean res = presenterImpl.validateFields();
		assertFalse(res);
		verify(view).checkAllFieldsValid();
		verify(entryChangePresenter, times(1)).validateFields();
	}

	@Test
	public void testSaveToModel() {
		List<TimesheetDayEntryChangePresenterEx> entryPresenters = new ArrayList<TimesheetDayEntryChangePresenterEx>();
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		entryPresenters.add(entryChangePresenter);
		presenterImpl.entryPresenters = entryPresenters;
		// prep done, test!
		presenterImpl.saveToModel();
		verify(entryChangePresenter, times(3)).saveToModel();
	}

	@Test
	public void testOnAdditionalEntry() {
		presenterImpl.onAdditionalEntry();
		assertEquals(4, timesheetDay.getEntries().size());
		verify(presenterFactory, times(1)).createTimesheetDayEntryChangePresenter(null);
		verify(entryChangePresenter, times(1)).setTimesheetDayEntry(any(TimesheetDayEntry.class));
		verify(entryChangePresenter, times(1)).setProjectList(anyListOf(Project.class));
		verify(entryChangePresenter, times(1)).setHoursChangeObserver(presenterImpl);
		verify(entryChangePresenter, times(1)).startPresenting();
		verify(view, times(1)).addTimesheetDayEntryView(any(TimesheetDayEntryChangeView.class));
	}

	@Test
	public void testHoursChanged() {
		presenterImpl.hoursChanged(project);
		verify(observer).hoursChanged(project);
	}

}
