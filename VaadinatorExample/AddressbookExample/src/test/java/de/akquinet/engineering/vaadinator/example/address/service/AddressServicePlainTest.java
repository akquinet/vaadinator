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
package de.akquinet.engineering.vaadinator.example.address.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.dao.AddressDao;
import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.AddressQuery;
import de.akquinet.engineering.vaadinator.example.address.model.Anreden;

public class AddressServicePlainTest extends AbstractServicePlainTest {

	AddressDao dao;
	AddressServicePlain service;
	AddressServicePlain serviceCascade;

	@Test
	public void testListAll() {
		List<Address> res = service.listAllAddress(new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals(Anreden.FRAU, res.get(0).getAnrede());
		assertEquals("Angela Merkel", res.get(0).getName());
		assertEquals("angie@chancellory.de", res.get(0).getEmail());
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(trans, atMost(1)).commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListAllBackend() {
		AddressService myBackend = mock(AddressService.class);
		service.backend = myBackend;
		service.listAllAddress(new HashMap<String, Object>());
		verify(myBackend, atMost(1)).listAllAddress(anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListAllError() {
		when(dao.list((AddressQuery) any(), anyMap())).thenThrow(new RuntimeException("Fehlerteufel"));
		boolean hasExc = false;
		try {
			service.listAllAddress(new HashMap<String, Object>());
		} catch (RuntimeException e) {
			if ("Fehlerteufel".equals(e.getMessage())) {
				hasExc = true;
			}
		}
		assertTrue(hasExc);
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(trans, never()).commit();
		verify(trans, atMost(1)).rollback();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateExisting() {
		Address address = service.listAllAddress(new HashMap<String, Object>()).get(0);
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(trans, atMost(1)).commit();
		address.setEmail("theoneandonly@schland.de");
		service.updateExistingAddress(address, new HashMap<String, Object>());
		verify(dao).merge(eq(address), anyMap());
		verify(emf, atMost(2)).createEntityManager();
		verify(em, atMost(2)).getTransaction();
		verify(trans, atMost(2)).begin();
		verify(trans, atMost(2)).commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateExistingBackend() {
		AddressService myBackend = mock(AddressService.class);
		service.backend = myBackend;
		service.updateExistingAddress(new Address(), new HashMap<String, Object>());
		verify(myBackend, atMost(1)).updateExistingAddress((Address) any(), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateExistingError() {
		when(dao.merge((Address) any(), anyMap())).thenThrow(new RuntimeException("Fehlerteufel"));
		Address address = service.listAllAddress(new HashMap<String, Object>()).get(0);
		verify(trans, atMost(1)).commit();
		address.setEmail("theoneandonly@schland.de");
		boolean hasExc = false;
		try {
			service.updateExistingAddress(address, new HashMap<String, Object>());
		} catch (RuntimeException e) {
			if ("Fehlerteufel".equals(e.getMessage())) {
				hasExc = true;
			}
		}
		assertTrue(hasExc);
		verify(dao).merge(eq(address), anyMap());
		verify(emf, atMost(2)).createEntityManager();
		verify(em, atMost(2)).getTransaction();
		verify(trans, atMost(2)).begin();
		verify(trans, atMost(1)).commit();
		verify(trans, atMost(1)).rollback();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddNew() {
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		service.addNewAddress(address, new HashMap<String, Object>());
		verify(dao).merge(eq(address), anyMap());
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(trans, atMost(1)).commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddNewBackend() {
		AddressService myBackend = mock(AddressService.class);
		service.backend = myBackend;
		service.addNewAddress(new Address(), new HashMap<String, Object>());
		verify(myBackend, atMost(1)).addNewAddress((Address) any(), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddNewError() {
		when(dao.merge((Address) any(), anyMap())).thenThrow(new RuntimeException("Fehlerteufel"));
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		boolean hasExc = false;
		try {
			service.addNewAddress(address, new HashMap<String, Object>());
		} catch (RuntimeException e) {
			if ("Fehlerteufel".equals(e.getMessage())) {
				hasExc = true;
			}
		}
		assertTrue(hasExc);
		verify(dao).merge(eq(address), anyMap());
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(trans, never()).commit();
		verify(trans, atMost(1)).rollback();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Before
	public void setUp() {
		super.setUp();
		dao = mock(AddressDao.class);
		when(dao.list((AddressQuery) any(), anyMap())).thenReturn(
				Collections.singletonList(new Address(Anreden.FRAU, "Angela", "Merkel", "angie@chancellory.de")));
		// object(s) under test
		service = new AddressServicePlain(emf, dao);
		serviceCascade = new AddressServicePlain(emf, dao);
		service.backend = serviceCascade;
	}

}
