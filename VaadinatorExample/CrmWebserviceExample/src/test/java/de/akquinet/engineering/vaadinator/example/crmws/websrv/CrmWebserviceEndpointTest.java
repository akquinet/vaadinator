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
package de.akquinet.engineering.vaadinator.example.crmws.websrv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.akquinet.engineering.vaadinator.example.crmws.dao.ContactDao;
import de.akquinet.engineering.vaadinator.example.crmws.dto.ContactDto;
import de.akquinet.engineering.vaadinator.example.crmws.dto.HistoryDto;
import de.akquinet.engineering.vaadinator.example.crmws.model.Contact;
import de.akquinet.engineering.vaadinator.example.crmws.model.ContactQuery;
import de.akquinet.engineering.vaadinator.example.crmws.model.History;

public class CrmWebserviceEndpointTest {

	@SuppressWarnings("deprecation")
	Date dateRef = new Date(1970, 0, 22);
	Contact contactRef;
	ContactDto contactDtoRef;

	ContactDao contactDao;
	CrmWebserviceEndpoint webservice;

	@Before
	public void setUp() {
		// reference data
		contactRef = new Contact();
		contactRef.setName("Hans Hummel");
		contactRef.setRev(2);
		contactRef.setScore(88);
		History historyRef = new History();
		historyRef.setText("mors, mors");
		historyRef.setTimestamp(dateRef);
		contactRef.addHistory(historyRef);
		contactDtoRef = new ContactDto();
		contactDtoRef.setId(11);
		contactDtoRef.setRevision(22);
		contactDtoRef.setName("Marge Simpson");
		HistoryDto historyDtoRef = new HistoryDto();
		historyDtoRef.setText("grrr");
		historyDtoRef.setTimestamp(dateRef);
		contactDtoRef.getHistory().add(historyDtoRef);
		// actual mocks and endpoint
		contactDao = mock(ContactDao.class);
		webservice = new CrmWebserviceEndpoint(contactDao);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetContracts() {
		when(contactDao.list(any(ContactQuery.class), anyMap())).thenReturn(Collections.singletonList(contactRef));
		List<ContactDto> res = webservice.getContracts(2, 1);
		verify(contactDao).list(argThat(new BaseMatcher<ContactQuery>() {

			@Override
			public boolean matches(Object arg0) {
				return arg0 != null && (arg0 instanceof ContactQuery) && ((ContactQuery) arg0).getFirstResult() == 2
						&& ((ContactQuery) arg0).getMaxResults() == 1;
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("ContactQuery 2-1");
			}
		}), anyMap());
		assertEquals(1, res.size());
		assertEquals("Hans Hummel", res.get(0).getName());
		assertEquals(0, res.get(0).getHistory().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetContactIncludingHistory() {
		when(contactDao.find(any(), anyMap())).thenReturn(contactRef);
		ContactDto res = webservice.getContactIncludingHistory(3);
		verify(contactDao).find(eq(new Long(3)), anyMap());
		assertNotNull(res);
		assertEquals("Hans Hummel", res.getName());
		assertEquals(1, res.getHistory().size());
		assertEquals("mors, mors", res.getHistory().get(0).getText());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddContact() {
		when(contactDao.merge(any(Contact.class), anyMap())).thenAnswer(new Answer<Contact>() {

			@Override
			public Contact answer(InvocationOnMock invocation) throws Throwable {
				Contact contactBase = (Contact) invocation.getArguments()[0];
				Contact contactRet = new Contact();
				contactRet.setRev(contactBase.getRev());
				contactRet.setName(contactBase.getName());
				contactRet.setHistory(contactBase.getHistory());
				// dirty but necessary
				Method idSetter = contactRet.getClass().getDeclaredMethod("setId", Long.TYPE);
				idSetter.setAccessible(true);
				idSetter.invoke(contactRet, 111);
				return contactRet;
			}
		});
		// now go
		ContactDto contactDtoNew = webservice.addContact(contactDtoRef);
		verify(contactDao).merge(argThat(new BaseMatcher<Contact>() {

			@Override
			public boolean matches(Object arg0) {
				return arg0 != null && (arg0 instanceof Contact) && ((Contact) arg0).getId() == 0 && ((Contact) arg0).getRev() == 1
						&& "Marge Simpson".equals(((Contact) arg0).getName()) && ((Contact) arg0).getHistory().size() == 0;
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("ID=0, Rev=1, Name=Marge Simpson, no hist");
			}
		}), anyMap());
		assertEquals(111, contactDtoNew.getId());
		assertEquals(1, contactDtoNew.getRevision());
		assertEquals("Marge Simpson", contactDtoNew.getName());
		assertEquals(0, contactDtoNew.getHistory().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateContact() {
		when(contactDao.find(any(), anyMap())).thenReturn(contactRef);
		when(contactDao.merge(any(Contact.class), anyMap())).thenAnswer(new Answer<Contact>() {

			@Override
			public Contact answer(InvocationOnMock invocation) throws Throwable {
				Contact contactRet = (Contact) invocation.getArguments()[0];
				// dirty but necessary
				Method idSetter = contactRet.getClass().getDeclaredMethod("setId", Long.TYPE);
				idSetter.setAccessible(true);
				idSetter.invoke(contactRet, 11);
				return contactRet;
			}
		});
		contactDtoRef.setRevision(contactRef.getRev());
		// now go
		ContactDto contactDtoNew = webservice.updateContact(contactDtoRef);
		verify(contactDao).find(eq(new Long(11)), anyMap());
		verify(contactDao).merge(argThat(new BaseMatcher<Contact>() {

			@Override
			public boolean matches(Object arg0) {
				return arg0 != null && (arg0 instanceof Contact) && ((Contact) arg0).getId() == 11 && ((Contact) arg0).getRev() == 3
						&& "Marge Simpson".equals(((Contact) arg0).getName()) && ((Contact) arg0).getHistory().size() == 1
						&& "mors, mors".equals(((Contact) arg0).getHistory().get(0).getText());
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("ID=11, Rev=3, Name=Marge Simpson, old hist");
			}
		}), anyMap());
		assertEquals(11, contactDtoNew.getId());
		assertEquals(3, contactDtoNew.getRevision());
		assertEquals("Marge Simpson", contactDtoNew.getName());
		assertEquals(0, contactDtoNew.getHistory().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateContactInvalidRev() {
		when(contactDao.find(any(), anyMap())).thenReturn(contactRef);
		contactDtoRef.setRevision(contactRef.getRev() + 1);
		// now go
		Exception exc = null;
		try {
			webservice.updateContact(contactDtoRef);
		} catch (Exception e) {
			exc = e;
		}
		verify(contactDao).find(eq(new Long(11)), anyMap());
		verify(contactDao, never()).merge(any(Contact.class), anyMap());
		assertNotNull(exc);
		assertTrue(exc.toString().contains("Try to work with outdated revision"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddHistoryToContact() {
		when(contactDao.find(any(), anyMap())).thenReturn(contactRef);
		when(contactDao.merge(any(Contact.class), anyMap())).thenAnswer(new Answer<Contact>() {

			@Override
			public Contact answer(InvocationOnMock invocation) throws Throwable {
				Contact contactRet = (Contact) invocation.getArguments()[0];
				// dirty but necessary
				Method idSetter = contactRet.getClass().getDeclaredMethod("setId", Long.TYPE);
				idSetter.setAccessible(true);
				idSetter.invoke(contactRet, 11);
				return contactRet;
			}
		});
		contactDtoRef.setRevision(contactRef.getRev());
		// now go
		ContactDto contactDtoNew = webservice.addHistoryToContact(contactDtoRef, contactDtoRef.getHistory().get(0), false);
		verify(contactDao).find(eq(new Long(11)), anyMap());
		verify(contactDao).merge(argThat(new BaseMatcher<Contact>() {

			@Override
			public boolean matches(Object arg0) {
				return arg0 != null && (arg0 instanceof Contact) && ((Contact) arg0).getId() == 11 && ((Contact) arg0).getRev() == 3
						&& "Hans Hummel".equals(((Contact) arg0).getName()) && ((Contact) arg0).getHistory().size() == 2
						&& "mors, mors".equals(((Contact) arg0).getHistory().get(0).getText())
						&& "grrr".equals(((Contact) arg0).getHistory().get(1).getText())
						&& dateRef.equals(((Contact) arg0).getHistory().get(1).getTimestamp());
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("ID=11, Rev=3, Name=Hans Hummel, old hist + new hist");
			}
		}), anyMap());
		assertEquals(11, contactDtoNew.getId());
		assertEquals(3, contactDtoNew.getRevision());
		assertEquals("Hans Hummel", contactDtoNew.getName());
		assertEquals(0, contactDtoNew.getHistory().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddHistoryToContactWithHist() {
		when(contactDao.find(any(), anyMap())).thenReturn(contactRef);
		when(contactDao.merge(any(Contact.class), anyMap())).thenAnswer(new Answer<Contact>() {

			@Override
			public Contact answer(InvocationOnMock invocation) throws Throwable {
				Contact contactRet = (Contact) invocation.getArguments()[0];
				// dirty but necessary
				Method idSetter = contactRet.getClass().getDeclaredMethod("setId", Long.TYPE);
				idSetter.setAccessible(true);
				idSetter.invoke(contactRet, 11);
				return contactRet;
			}
		});
		contactDtoRef.setRevision(contactRef.getRev());
		// now go
		ContactDto contactDtoNew = webservice.addHistoryToContact(contactDtoRef, contactDtoRef.getHistory().get(0), true);
		verify(contactDao).find(eq(new Long(11)), anyMap());
		verify(contactDao).merge(argThat(new BaseMatcher<Contact>() {

			@Override
			public boolean matches(Object arg0) {
				return arg0 != null && (arg0 instanceof Contact) && ((Contact) arg0).getId() == 11 && ((Contact) arg0).getRev() == 3
						&& "Hans Hummel".equals(((Contact) arg0).getName()) && ((Contact) arg0).getHistory().size() == 2
						&& "mors, mors".equals(((Contact) arg0).getHistory().get(0).getText())
						&& "grrr".equals(((Contact) arg0).getHistory().get(1).getText())
						&& dateRef.equals(((Contact) arg0).getHistory().get(1).getTimestamp());
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("ID=11, Rev=3, Name=Hans Hummel, old hist + new hist");
			}
		}), anyMap());
		assertEquals(11, contactDtoNew.getId());
		assertEquals(3, contactDtoNew.getRevision());
		assertEquals("Hans Hummel", contactDtoNew.getName());
		assertEquals(2, contactDtoNew.getHistory().size());
		assertEquals("mors, mors", contactDtoNew.getHistory().get(0).getText());
		assertEquals(dateRef, contactDtoNew.getHistory().get(0).getTimestamp());
		assertEquals("grrr", contactDtoNew.getHistory().get(1).getText());
		assertEquals(dateRef, contactDtoNew.getHistory().get(1).getTimestamp());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddHistoryToContactInvalidRev() {
		when(contactDao.find(any(), anyMap())).thenReturn(contactRef);
		contactDtoRef.setRevision(contactRef.getRev() + 1);
		// now go
		Exception exc = null;
		try {
			webservice.addHistoryToContact(contactDtoRef, contactDtoRef.getHistory().get(0), false);
		} catch (Exception e) {
			exc = e;
		}
		verify(contactDao).find(eq(new Long(11)), anyMap());
		verify(contactDao, never()).merge(any(Contact.class), anyMap());
		assertNotNull(exc);
		assertTrue(exc.toString().contains("Try to work with outdated revision"));
	}

}
