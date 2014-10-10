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
package de.akquinet.engineering.vaadinator.timesheet.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.service.CouchDbProjectService.ProjectCouch;

public class CouchDbProjectServiceTest {

	Map<String, String> getResults = new HashMap<String, String>();
	List<String> getUrls = new ArrayList<String>();

	CouchDbProjectService projectService;

	@Before
	public void setUp() {
		projectService = new CouchDbProjectService() {

			@Override
			protected JSONObject getCouch(String urlPart) throws IOException, MalformedURLException, JSONException {
				getUrls.add(urlPart);
				return getResults.containsKey(urlPart) ? (new JSONObject(getResults.get(urlPart))) : null;
			}

			@Override
			protected JSONObject putCouch(String urlPart, JSONObject content) throws IOException, MalformedURLException, JSONException {
				return null;
			}

		};
	}

	@Test
	public void testListAllProject() {
		getResults
				.put("_all_docs?startkey=\"project_\"&endkey=\"project_zzz\"",
						"{\"total_rows\":9,\"offset\":2,\"rows\":[{\"id\":\"project_P1\",\"key\":\"project_P1\",\"value\":{\"rev\":\"1-967a00dff5e02add41819138abb3284d\"}},{\"id\":\"project_P2\",\"key\":\"project_P2\",\"value\":{\"rev\":\"1-967a00dff5e02add41819138abb3284d\"}}]}");
		List<Project> res = projectService.listAllProject(new HashMap<String, Object>());
		assertEquals(1, getUrls.size());
		assertEquals("_all_docs?startkey=\"project_\"&endkey=\"project_zzz\"", getUrls.get(0));
		assertEquals(2, res.size());
		assertEquals("P1", res.get(0).getName());
		assertEquals("P2", res.get(1).getName());
		assertEquals(ProjectCouch.class, res.get(1).getClass());
		assertEquals("project_P2", ((ProjectCouch) res.get(1)).getCouchId());
		assertEquals("1-967a00dff5e02add41819138abb3284d", ((ProjectCouch) res.get(1)).getCouchRev());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddNewProject() {
		projectService.addNewProject(new Project(), new HashMap<String, Object>());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUpdateExistingProject() {
		projectService.updateExistingProject(new Project(), new HashMap<String, Object>());
	}

}
