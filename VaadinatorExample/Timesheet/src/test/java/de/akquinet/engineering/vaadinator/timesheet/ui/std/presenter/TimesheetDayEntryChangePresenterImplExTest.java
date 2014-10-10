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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.TimesheetDayEntry;
import de.akquinet.engineering.vaadinator.timesheet.ui.std.view.TimesheetDayEntryChangeViewEx;

public class TimesheetDayEntryChangePresenterImplExTest {

	TimesheetDayEntryChangeViewEx view;
	HoursChangeObserver observer;

	Project project;
	TimesheetDayEntry dayEntry;

	TimesheetDayEntryChangePresenterImplEx presenterImpl;

	@Before
	public void setUp() {
		view = mock(TimesheetDayEntryChangeViewEx.class);
		observer = mock(HoursChangeObserver.class);
		project = new Project("test");
		dayEntry = new TimesheetDayEntry();
		dayEntry.setHours("01:00");
		dayEntry.setProject(project);
		presenterImpl = new TimesheetDayEntryChangePresenterImplEx(new HashMap<String, Object>(), view, null, null, null);
		presenterImpl.setHoursChangeObserver(observer);
		presenterImpl.setProjectList(Collections.singletonList(project));
		presenterImpl.setTimesheetDayEntry(dayEntry);
	}

	@Test
	public void testStartPresenting() {
		presenterImpl.startPresenting();
		verify(view).setObserver(presenterImpl);
		verify(view).initializeUi();
		verify(view).setChoicesForProject(anyListOf(Project.class));
		verify(view).setProject(project);
		verify(view).setHours("01:00");
	}

	@Test
	public void testStartPresentingNulls() {
		dayEntry.setHours(null);
		dayEntry.setProject(null);
		presenterImpl.startPresenting();
		verify(view).setObserver(presenterImpl);
		verify(view).initializeUi();
		verify(view).setChoicesForProject(anyListOf(Project.class));
		verify(view).setProject(null);
		verify(view).setHours("00:00");
	}

	@Test
	public void testLoadFromModel() {
		presenterImpl.loadFromModel();
		assertEquals(project, presenterImpl.oldProject);
		verify(view).setProject(project);
		verify(view).setHours("01:00");
	}

	@Test
	public void testLoadFromModelNulls() {
		dayEntry.setHours(null);
		dayEntry.setProject(null);
		// prep done, test!
		presenterImpl.loadFromModel();
		assertNull(presenterImpl.oldProject);
		verify(view).setProject(null);
		verify(view).setHours("00:00");
	}

	@Test
	public void testValidateFields() {
		presenterImpl.validateFields();
		verify(view).checkAllFieldsValid();
	}

	@Test
	public void testSaveToModel() {
		Project alt = new Project("alt");
		when(view.getProject()).thenReturn(alt);
		when(view.getHours()).thenReturn("2:22");
		// prep done, test!
		presenterImpl.saveToModel();
		assertEquals(alt, dayEntry.getProject());
		assertEquals("2:22", dayEntry.getHours());
		verify(view).getProject();
		verify(view).getHours();
	}

	@Test
	public void testOnProjectChanged() {
		presenterImpl.presenting = true;
		presenterImpl.oldProject = project;
		// prep done, test!
		Project alt = new Project("alt");
		when(view.getProject()).thenReturn(alt);
		when(view.getHours()).thenReturn("01:00");
		presenterImpl.onProjectChanged();
		assertEquals(alt, dayEntry.getProject());
		assertEquals("01:00", dayEntry.getHours());
		verify(view).getProject();
		verify(observer).hoursChanged(alt);
		verify(observer).hoursChanged(project);
	}

	@Test
	public void testOnProjectChangedNoOld() {
		presenterImpl.presenting = true;
		presenterImpl.oldProject = null;
		// prep done, test!
		Project alt = new Project("alt");
		when(view.getProject()).thenReturn(alt);
		when(view.getHours()).thenReturn("01:00");
		presenterImpl.onProjectChanged();
		assertEquals(alt, dayEntry.getProject());
		assertEquals("01:00", dayEntry.getHours());
		verify(view).getProject();
		verify(observer).hoursChanged(alt);
		verify(observer, never()).hoursChanged(project);
	}

	@Test
	public void testOnProjectChangedNotPresenting() {
		presenterImpl.presenting = false;
		presenterImpl.oldProject = null;
		// prep done, test!
		presenterImpl.onProjectChanged();
		verify(view, never()).getProject();
		verify(observer, never()).hoursChanged(any(Project.class));
	}

	@Test
	public void testOnHoursChanged() {
		presenterImpl.presenting = true;
		when(view.getProject()).thenReturn(project);
		when(view.getHours()).thenReturn("2:22");
		// prep done, test!
		presenterImpl.onHoursChanged();
		assertEquals("2:22", dayEntry.getHours());
		verify(view).getHours();
		verify(observer).hoursChanged(project);
	}

	@Test
	public void testOnHoursChangedNoProject() {
		presenterImpl.presenting = true;
		when(view.getProject()).thenReturn(null);
		when(view.getHours()).thenReturn("2:22");
		// prep done, test!
		presenterImpl.onHoursChanged();
		assertEquals("2:22", dayEntry.getHours());
		verify(view).getHours();
		verify(observer, never()).hoursChanged(project);
	}

	@Test
	public void testOnHoursChangedNotPresenting() {
		presenterImpl.presenting = false;
		// prep done, test!
		presenterImpl.onHoursChanged();
		verify(view, never()).getHours();
		verify(observer, never()).hoursChanged(project);
	}

}
