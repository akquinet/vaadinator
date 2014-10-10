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
package de.akquinet.engineering.vaadinator.example.crmws.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.crmws.dto.ContactDto;

public class ContactMapperExTest {

	@SuppressWarnings("deprecation")
	Date dateRef = new Date(1970, 0, 22);
	Contact contactRef;

	ContactMapperEx mapper;

	@Before
	public void setUp() {
		mapper = new ContactMapperEx();
		contactRef = new Contact();
		contactRef.setId(3);
		contactRef.setName("Hans Hummel");
		contactRef.setRev(2);
		contactRef.setScore(88);
		History historyRef = new History();
		historyRef.setId(33);
		historyRef.setText("mors, mors");
		historyRef.setTimestamp(dateRef);
		contactRef.addHistory(historyRef);
	}

	@Test
	public void testMapContactWithoutHistory() {
		ContactDto dto = new ContactDto();
		mapper.mapContactWithoutHistory(contactRef, dto);
		assertEquals(3, dto.getId());
		assertEquals("Hans Hummel", dto.getName());
		assertEquals(2, dto.getRevision());
		assertEquals(0, dto.getHistory().size());
	}

	@Test
	public void testMapContactWithoutHistoryInbound() {
		// prep work (secured by test above)
		ContactDto dto = new ContactDto();
		mapper.mapContactWithoutHistory(contactRef, dto);
		// actual test
		Contact contact = new Contact();
		mapper.mapContactWithoutHistoryInbound(dto, contact);
		assertEquals(0 /* !! */, contact.getId());
		assertEquals("Hans Hummel", contact.getName());
		assertEquals(2, contact.getRev());
		assertEquals(0, dto.getHistory().size());
	}

	@Test
	public void testMapContactInclHistory() {
		ContactDto dto = new ContactDto();
		mapper.mapContactInclHistory(contactRef, dto);
		assertEquals(3, dto.getId());
		assertEquals("Hans Hummel", dto.getName());
		assertEquals(2, dto.getRevision());
		assertEquals(1, dto.getHistory().size());
		assertEquals(33, dto.getHistory().get(0).getId());
		assertEquals("mors, mors", dto.getHistory().get(0).getText());
		assertEquals(dateRef, dto.getHistory().get(0).getTimestamp());
	}

}
