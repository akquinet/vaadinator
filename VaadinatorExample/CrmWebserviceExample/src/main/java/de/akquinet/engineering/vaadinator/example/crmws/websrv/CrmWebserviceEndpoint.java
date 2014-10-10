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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import de.akquinet.engineering.vaadinator.example.crmws.dao.ContactDao;
import de.akquinet.engineering.vaadinator.example.crmws.dao.ContactDaoEjb;
import de.akquinet.engineering.vaadinator.example.crmws.dto.ContactDto;
import de.akquinet.engineering.vaadinator.example.crmws.dto.HistoryDto;
import de.akquinet.engineering.vaadinator.example.crmws.model.Contact;
import de.akquinet.engineering.vaadinator.example.crmws.model.ContactMapper;
import de.akquinet.engineering.vaadinator.example.crmws.model.ContactMapperEx;
import de.akquinet.engineering.vaadinator.example.crmws.model.ContactQuery;
import de.akquinet.engineering.vaadinator.example.crmws.model.History;
import de.akquinet.engineering.vaadinator.example.crmws.model.HistoryMapper;

@WebService
public class CrmWebserviceEndpoint {

	public CrmWebserviceEndpoint() {
		super();
	}

	public CrmWebserviceEndpoint(ContactDao contactDao) {
		super();
		this.contactDao = contactDao;
	}

	private ContactDao contactDao;

	@Inject
	@WebMethod(exclude = true)
	public void setContactDao(ContactDaoEjb contactDao) {
		this.contactDao = contactDao;
	}

	@WebMethod
	public List<ContactDto> getContracts(@WebParam(name = "from") int from, @WebParam(name = "limit") int limit) {
		ContactQuery contactQuery = new ContactQuery();
		contactQuery.setFirstResult(from);
		contactQuery.setMaxResults(limit);
		List<Contact> databaseContacts = contactDao.list(contactQuery, new HashMap<String, Object>());
		List<ContactDto> deliveredContacts = new ArrayList<ContactDto>();
		for (Contact contact : databaseContacts) {
			ContactDto contactDto = new ContactDto();
			obtainContactMapper().mapContactWithoutHistory(contact, contactDto);
			deliveredContacts.add(contactDto);
		}
		return deliveredContacts;
	}

	@WebMethod
	public ContactDto getContactIncludingHistory(@WebParam(name = "id") long id) {
		Contact contact = contactDao.find(id, new HashMap<String, Object>());
		ContactDto contactDto = new ContactDto();
		obtainContactMapper().mapContactInclHistory(contact, contactDto);
		return contactDto;
	}

	@WebMethod
	public ContactDto addContact(@WebParam(name = "contactDto") ContactDto contactDto) {
		Contact contact = new Contact();
		obtainContactMapper().mapContactWithoutHistoryInbound(contactDto, contact);
		contact.setRev(0);
		incrementRevision(contact);
		contact = contactDao.merge(contact, new HashMap<String, Object>());
		contactDto = new ContactDto();
		obtainContactMapper().mapContactWithoutHistory(contact, contactDto);
		return contactDto;
	}

	@WebMethod
	public ContactDto updateContact(@WebParam(name = "contactDto") ContactDto contactDto) {
		Contact contact = contactDao.find(contactDto.getId(), new HashMap<String, Object>());
		checkRevision(contactDto, contact);
		obtainContactMapper().mapContactWithoutHistoryInbound(contactDto, contact);
		incrementRevision(contact);
		contact = contactDao.merge(contact, new HashMap<String, Object>());
		contactDto = new ContactDto();
		obtainContactMapper().mapContactWithoutHistory(contact, contactDto);
		return contactDto;
	}

	@WebMethod
	public ContactDto addHistoryToContact(@WebParam(name = "contactDto") ContactDto contactDto, @WebParam(name = "historyDto") HistoryDto historyDto,
			@WebParam(name = "returnHistory") boolean returnHistory) {
		Contact contact = contactDao.find(contactDto.getId(), new HashMap<String, Object>());
		checkRevision(contactDto, contact);
		History history = new History();
		obtainHistoryMapper().mapHistoryOnlyInbound(historyDto, history);
		contact.addHistory(history);
		incrementRevision(contact);
		contact = contactDao.merge(contact, new HashMap<String, Object>());
		contactDto = new ContactDto();
		if (returnHistory) {
			obtainContactMapper().mapContactInclHistory(contact, contactDto);
		} else {
			obtainContactMapper().mapContactWithoutHistory(contact, contactDto);
		}
		return contactDto;
	}

	private void checkRevision(ContactDto contactDto, Contact contact) {
		if (contact.getRev() != contactDto.getRevision()) {
			throw new RuntimeException("Try to work with outdated revision " + contactDto.getRevision() + ". Current is " + contact.getRev() + ".");
		}
	}

	private void incrementRevision(Contact contact) {
		contact.setRev(contact.getRev() + 1);
	}

	protected ContactMapper obtainContactMapper() {
		return new ContactMapperEx();
	}

	protected HistoryMapper obtainHistoryMapper() {
		return new HistoryMapper();
	}

}
