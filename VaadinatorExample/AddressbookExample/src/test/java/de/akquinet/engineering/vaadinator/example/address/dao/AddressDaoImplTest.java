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
package de.akquinet.engineering.vaadinator.example.address.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.AddressProperties;
import de.akquinet.engineering.vaadinator.example.address.model.AddressQuery;
import de.akquinet.engineering.vaadinator.example.address.model.Anreden;

public class AddressDaoImplTest extends AbstractDaoImplTest {

	private AddressDaoImpl dao;

	@Test
	public void testFilterOr() {
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		dao.merge(address, new HashMap<String, Object>());
		
		AddressQuery addressQuery = new AddressQuery();
		addressQuery.setEmail("angie%");
		addressQuery.setVorname("Gerd");
		addressQuery.setOperator(AddressQuery.OR);
		
		long count = dao.count(addressQuery, new HashMap<String, Object>());
		
		assertEquals(2, count);
	}
	
	@Test
	public void testCountAll() {
		long count = dao.count(new AddressQuery(), new HashMap<String, Object>());
		
		assertEquals(1, count);
		
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		dao.merge(address, new HashMap<String, Object>());
		
		count = dao.count(new AddressQuery(), new HashMap<String, Object>());
		
		assertEquals(2, count);
	}

	@Test
	public void testCountFilterEmail() {
		AddressQuery addressQuery = new AddressQuery();
		addressQuery.setEmail("angie%");
		
		long count = dao.count(addressQuery, new HashMap<String, Object>());
		
		assertEquals(1, count);
		
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		dao.merge(address, new HashMap<String, Object>());
		
		count = dao.count(addressQuery, new HashMap<String, Object>());
		
		assertEquals(1, count);
	}
	@Test
	public void testListAll() {
		List<Address> res = dao.list(new AddressQuery(), new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals(Anreden.FRAU, res.get(0).getAnrede());
		assertEquals("Angela Merkel", res.get(0).getName());
		assertEquals("angie@chancellory.de", res.get(0).getEmail());
	}

	@Test
	public void testListFilterNumber() {
		em.createNativeQuery(
				"insert into address (id, anrede, vorname, nachname, email) values (2, " + Anreden.FRAU.ordinal()
						+ ", 'Angela', 'Merkel', 'angie2@chancellory.de')").executeUpdate();
		AddressQuery query = new AddressQuery();
		query.setIdFrom(new Long(2));
		List<Address> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals("angie2@chancellory.de", res.get(0).getEmail());
	}

	@Test
	public void testListFilterString() {
		em.createNativeQuery(
				"insert into address (id, anrede, vorname, nachname, email) values (2, " + Anreden.FRAU.ordinal()
						+ ", 'Angela', 'Merkel', 'angie2@chancellory.de')").executeUpdate();
		AddressQuery query = new AddressQuery();
		query.setEmail("%angie2%");
		List<Address> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals("angie2@chancellory.de", res.get(0).getEmail());
	}

	@Test
	public void testListPage() {
		em.createNativeQuery(
				"insert into address (id, anrede, vorname, nachname, email) values (2, " + Anreden.FRAU.ordinal()
						+ ", 'Angela', 'Merkel', 'angie2@chancellory.de')").executeUpdate();
		AddressQuery query = new AddressQuery();
		query.setMaxResults(1);
		List<Address> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals("Angela Merkel", res.get(0).getName());
	}

	@Test
	public void testListSortDesc() {
		em.createNativeQuery(
				"insert into address (id, anrede, vorname, nachname, email) values (2, " + Anreden.FRAU.ordinal()
						+ ", 'Berta', 'Merkel', 'berta@chancellory.de')").executeUpdate();
		AddressQuery query = new AddressQuery();
		query.setOrderBy(AddressProperties.VORNAME);
		query.setOrderDescending(true);
		List<Address> res = dao.list(query, new HashMap<String, Object>());
		assertEquals(2, res.size());
		assertEquals("Berta Merkel", res.get(0).getName());
	}

	@Test
	public void testMergeExisting() {
		Address address = dao.list(new AddressQuery(), new HashMap<String, Object>()).get(0);
		address.setEmail("theoneandonly@schland.de");
		dao.merge(address, new HashMap<String, Object>());
		assertEquals("theoneandonly@schland.de", em.createNativeQuery("select email from address where id=1").getResultList().get(0).toString());
	}

	@Test
	public void testMergeNew() {
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		dao.merge(address, new HashMap<String, Object>());
		assertEquals("mrgazprom@gmail.ru", em.createNativeQuery("select email from address where id=(select max(id) from address)").getResultList()
				.get(0).toString());
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		em.createNativeQuery(
				"insert into address (id, anrede, vorname, nachname, email) values (1, " + Anreden.FRAU.ordinal()
						+ ", 'Angela', 'Merkel', 'angie@chancellory.de')").executeUpdate();
		// object(s) under test
		dao = new AddressDaoImpl(em);
	}

}
