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
package de.akquinet.engineering.vaadinator.example.crmws.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.crmws.model.Contact;
import de.akquinet.engineering.vaadinator.example.crmws.model.ContactProperties;
import de.akquinet.engineering.vaadinator.example.crmws.model.ContactQuery;

public class ContactDaoImplTest extends AbstractDaoImplTest {

	ContactDaoImpl dao;

	@Test
	public void testListAll() {
		List<Contact> res = dao.list(new ContactQuery(), new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals(1, res.get(0).getId());
		assertEquals("Angela Merkel", res.get(0).getName());
		assertEquals(2, res.get(0).getRev());
		assertEquals(88, res.get(0).getScore());
		assertEquals(1, res.get(0).getHistory().size());
		assertEquals(2, res.get(0).getHistory().get(0).getId());
		assertEquals("Schach mit Lukas Podolski", res.get(0).getHistory().get(0).getText());
		assertNotNull(res.get(0).getHistory().get(0).getTimestamp());
	}

	@Test
	public void testListFilterNumber() {
		em.createNativeQuery("insert into crmcontact (id, name, rev, score) values (2, 'Gerd Schröder', 2, 0)").executeUpdate();
		ContactQuery query = new ContactQuery();
		query.setIdFrom(new Long(2));
		List<Contact> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals("Gerd Schröder", res.get(0).getName());
	}

	@Test
	public void testListFilterString() {
		em.createNativeQuery("insert into crmcontact (id, name, rev, score) values (2, 'Gerd Schröder', 2, 0)").executeUpdate();
		ContactQuery query = new ContactQuery();
		query.setName("Gerd%");
		List<Contact> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals("Gerd Schröder", res.get(0).getName());
	}

	@Test
	public void testListPage() {
		em.createNativeQuery("insert into crmcontact (id, name, rev, score) values (2, 'Gerd Schröder', 2, 0)").executeUpdate();
		ContactQuery query = new ContactQuery();
		query.setMaxResults(1);
		List<Contact> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals("Angela Merkel", res.get(0).getName());
	}

	@Test
	public void testListSortDesc() {
		em.createNativeQuery("insert into crmcontact (id, name, rev, score) values (2, 'Gerd Schröder', 2, 0)").executeUpdate();
		ContactQuery query = new ContactQuery();
		query.setOrderBy(ContactProperties.NAME);
		query.setOrderDescending(true);
		List<Contact> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(2, res.size());
		assertEquals("Gerd Schröder", res.get(0).getName());
	}

	@Test
	public void testMergeExisting() {
		Contact contact = dao.list(new ContactQuery(), new HashMap<String, Object>()).get(0);
		contact.setName("Ursula von der Leyen");
		dao.merge(contact, new HashMap<String, Object>());
		assertEquals("Ursula von der Leyen", em.createNativeQuery("select name from crmcontact where id=1").getResultList().get(0).toString());
	}

	@Test
	public void testMergeNew() {
		Contact contact = new Contact();
		contact.setName("Ursula von der Leyen");
		dao.merge(contact, new HashMap<String, Object>());
		assertEquals("Ursula von der Leyen", em.createNativeQuery("select name from crmcontact where id=(select max(id) from crmcontact)")
				.getResultList().get(0).toString());
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		em.createNativeQuery("insert into crmcontact (id, name, rev, score) values (1, 'Angela Merkel', 2, 88)").executeUpdate();
		em.createNativeQuery("insert into crmhistory (id, text, timestamp) values (2, 'Schach mit Lukas Podolski', now())").executeUpdate();
		em.createNativeQuery("insert into crmcontact_crmhistory (contact_id, history_id) values (1, 2)").executeUpdate();
		// object(s) under test
		dao = new ContactDaoImpl(em);
	}

}
