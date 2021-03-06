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

import java.util.List;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;

public interface TimesheetDayChangePresenterEx extends TimesheetDayChangePresenter, CascadingCapablePresenter {

	public List<Project> getProjectList();

	public void setProjectList(List<Project> projectList);
	
	public void setHoursChangeObserver(HoursChangeObserver observer);

}
