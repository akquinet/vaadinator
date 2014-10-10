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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
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

public class AddressServiceImplTest {

	AddressDao dao;
	AddressServiceImpl service;

	@Test
	public void testListAll() {
		List<Address> res = service.listAllAddress(new HashMap<String, Object>());
		assertEquals(1, res.size());
		assertEquals(Anreden.FRAU, res.get(0).getAnrede());
		assertEquals("Angela Merkel", res.get(0).getName());
		assertEquals("angie@chancellory.de", res.get(0).getEmail());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateExisting() {
		Address address = service.listAllAddress(new HashMap<String, Object>()).get(0);
		address.setEmail("theoneandonly@schland.de");
		service.updateExistingAddress(address, new HashMap<String, Object>());
		verify(dao).merge(eq(address), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddNew() {
		Address address = new Address(Anreden.HERR, "Gerd", "Schröder", "mrgazprom@gmail.ru");
		service.addNewAddress(address, new HashMap<String, Object>());
		verify(dao).merge(eq(address), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		dao = mock(AddressDao.class);
		when(dao.list((AddressQuery) any(), anyMap())).thenReturn(
				Collections.singletonList(new Address(Anreden.FRAU, "Angela", "Merkel", "angie@chancellory.de")));
		// object(s) under test
		service = new AddressServiceImpl(dao);
	}

}
