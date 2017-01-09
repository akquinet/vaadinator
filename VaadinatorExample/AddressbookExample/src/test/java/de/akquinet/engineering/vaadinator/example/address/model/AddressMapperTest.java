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
package de.akquinet.engineering.vaadinator.example.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class AddressMapperTest {

	@SuppressWarnings("deprecation")
	private Date dateRef = new Date(1970, 0, 22);
	private Address addressRef;

	private AddressMapper mapper;

	@Before
	public void setUp() {
		mapper = new AddressMapper();
		addressRef = new Address();
		addressRef.setAnrede(Anreden.HERR);
		addressRef.setEmail("abu@desert.com");
		addressRef.setGeburtsdatum(dateRef);
		addressRef.setId(22);
		addressRef.setNachname("bin Reich");
		addressRef.setVorname("Abu");
	}

	@Test
	public void testMapFull() {
		Address address = new Address();
		mapper.mapFull(addressRef, address);
		assertEquals(Anreden.HERR, address.getAnrede());
		assertEquals("abu@desert.com", address.getEmail());
		assertEquals(dateRef, address.getGeburtsdatum());
		assertEquals(22, address.getId());
		assertEquals("bin Reich", address.getNachname());
		assertEquals("Abu", address.getVorname());
	}

	@Test
	public void testMapRestricted() {
		Address address = new Address();
		mapper.mapRestricted(addressRef, address);
		assertEquals(Anreden.HERR, address.getAnrede());
		assertEquals(22, address.getId());
		assertEquals("bin Reich", address.getNachname());
		assertEquals("Abu", address.getVorname());
		assertNull(address.getGeburtsdatum());
		assertNull(address.getEmail());
	}

	@Test
	public void testMapMini() {
		Address address = new Address();
		mapper.mapMini(addressRef, address);
		assertEquals("Abu", address.getVorname());
		assertNull(address.getGeburtsdatum());
		assertNull(address.getEmail());
		assertNull(address.getAnrede());
		assertEquals(0, address.getId());
		assertNull(address.getNachname());
	}

}
