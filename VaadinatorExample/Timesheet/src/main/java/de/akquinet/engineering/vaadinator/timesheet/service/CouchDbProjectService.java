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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.akquinet.engineering.vaadinator.timesheet.model.Project;
import de.akquinet.engineering.vaadinator.timesheet.model.ProjectQuery;

public class CouchDbProjectService extends AbstractCouchDbService implements ProjectService {

	public CouchDbProjectService() {
		super();
	}

	@Override
	public List<Project> listAllProject(Map<String, Object> context) {
		try {
			JSONObject couchObj = getCouch("_all_docs?startkey=\"project_\"&endkey=\"project_zzz\"");
			JSONArray couchProjList = couchObj.getJSONArray("rows");
			List<Project> res = new ArrayList<Project>();
			for (int i = 0; i < couchProjList.length(); i++) {
				JSONObject couchProj = couchProjList.getJSONObject(i);
				res.add(new ProjectCouch(couchProj.getString("id"), couchProj.getJSONObject("value").getString("rev"), couchProj.getString("id")
						.substring("project_".length())));
			}
			return res;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Project addNewProject(Project project, Map<String, Object> context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Project updateExistingProject(Project project, Map<String, Object> context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeExistingProject(Project project,
			Map<String, Object> context) {
		// TODO Auto-generated method stub

	}

	@Override
	public long countProject(ProjectQuery query, Map<String, Object> context) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<Project> listProject(ProjectQuery query, Map<String, Object> context) {
		throw new UnsupportedOperationException();
	}

	public static class ProjectCouch extends Project {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ProjectCouch() {
			super();
		}

		public ProjectCouch(String couchId, String couchRev) {
			super();
			this.couchId = couchId;
			this.couchRev = couchRev;
		}

		public ProjectCouch(String couchId, String couchRev, String name) {
			super(name);
			this.couchId = couchId;
			this.couchRev = couchRev;
		}

		private String couchId;
		private String couchRev;

		public String getCouchId() {
			return couchId;
		}

		public void setCouchId(String couchId) {
			this.couchId = couchId;
		}

		public String getCouchRev() {
			return couchRev;
		}

		public void setCouchRev(String couchRev) {
			this.couchRev = couchRev;
		}
	}

}
