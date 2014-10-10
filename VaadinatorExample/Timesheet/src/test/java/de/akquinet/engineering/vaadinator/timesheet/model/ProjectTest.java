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
package de.akquinet.engineering.vaadinator.timesheet.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.akquinet.engineering.vaadinator.timesheet.service.CouchDbProjectService.ProjectCouch;

public class ProjectTest {

	Project project = new Project();

	@Test
	public void testEquals() {
		project.setName("bla");
		assertTrue(project.equals(new Project("bla")));
	}

	@Test
	public void testEqualsCouch() {
		project.setName("bla");
		assertTrue(project.equals(new ProjectCouch("1", "1-234", "bla")));
	}

	@Test
	public void testHashCode() {
		project.setName("bla");
		assertEquals("bla".hashCode() + 31, project.hashCode());
	}

	@Test
	public void testHashCodeCouch() {
		project.setName("bla");
		assertEquals("bla".hashCode() + 31, (new ProjectCouch("1", "1-234", "bla")).hashCode());
	}

}
