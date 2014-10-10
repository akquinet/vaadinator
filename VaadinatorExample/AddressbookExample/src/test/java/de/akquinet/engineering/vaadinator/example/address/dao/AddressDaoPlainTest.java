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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.AddressQuery;
import de.akquinet.engineering.vaadinator.example.address.model.Anreden;

public class AddressDaoPlainTest extends AbstractDaoPlainTest {

	AddressDao backend;
	AddressDaoPlain dao;
	AddressDaoPlain daoCascade;

	@SuppressWarnings("unchecked")
	@Test
	public void testListAll() {
		dao.list(new AddressQuery(), new HashMap<String, Object>());
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(backend, atMost(1)).list((AddressQuery) any(), anyMap());
		verify(trans, atMost(1)).commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListAllBackend() {
		AddressDao myBackend = mock(AddressDao.class);
		dao.backend = myBackend;
		dao.list(new AddressQuery(), new HashMap<String, Object>());
		verify(myBackend, atMost(1)).list((AddressQuery) any(), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListAllError() {
		when(backend.list((AddressQuery) any(), anyMap())).thenThrow(new RuntimeException("Fehlerteufel"));
		boolean hasExc = false;
		try {
			dao.list(new AddressQuery(), new HashMap<String, Object>());
		} catch (RuntimeException e) {
			if ("Fehlerteufel".equals(e.getMessage())) {
				hasExc = true;
			}
		}
		assertTrue(hasExc);
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(backend, atMost(1)).list((AddressQuery) any(), anyMap());
		verify(trans, never()).commit();
		verify(trans, atMost(1)).rollback();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMerge() {
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		dao.merge(address, new HashMap<String, Object>());
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(backend, atMost(1)).merge(eq(address), anyMap());
		verify(trans, atMost(1)).commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMergeBackend() {
		AddressDao myBackend = mock(AddressDao.class);
		dao.backend = myBackend;
		dao.merge(new Address(), new HashMap<String, Object>());
		verify(myBackend, atMost(1)).merge((Address) any(), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMergeError() {
		when(backend.merge((Address) any(), anyMap())).thenThrow(new RuntimeException("Fehlerteufel"));
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		boolean hasExc = false;
		try {
			dao.merge(address, new HashMap<String, Object>());
		} catch (RuntimeException e) {
			if ("Fehlerteufel".equals(e.getMessage())) {
				hasExc = true;
			}
		}
		assertTrue(hasExc);
		verify(emf, atMost(1)).createEntityManager();
		verify(em, atMost(1)).getTransaction();
		verify(trans, atMost(1)).begin();
		verify(backend, atMost(1)).merge(eq(address), anyMap());
		verify(trans, never()).commit();
		verify(trans, atMost(1)).rollback();
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		backend = mock(AddressDao.class);
		// object(s) under test
		dao = new AddressDaoPlain(emf);
		daoCascade = new AddressDaoPlain(emf);
		dao.backend = daoCascade;
		daoCascade.backend = backend;
	}
}
