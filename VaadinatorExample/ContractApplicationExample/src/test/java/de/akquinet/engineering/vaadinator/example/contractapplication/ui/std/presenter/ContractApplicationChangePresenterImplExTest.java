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
package de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.presenter;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.contractapplication.model.ContractApplication;
import de.akquinet.engineering.vaadinator.example.contractapplication.service.ContractApplicationService;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.ContractApplicationChangeView;

public class ContractApplicationChangePresenterImplExTest {

	ContractApplicationChangeView view;
	Presenter returnPres;
	ContractApplicationService service;
	ContractApplicationChangePresenterImplEx pres;

	@Before
	public void setUp() {
		view = mock(ContractApplicationChangeView.class);
		returnPres = mock(Presenter.class);
		service = mock(ContractApplicationService.class);
		pres = new ContractApplicationChangePresenterImplEx(new HashMap<String, Object>(), view, returnPres, service);
	}

	@Test
	public void testStartPresenting() {
		ContractApplication testApplication = new ContractApplication();
		testApplication.setCustomerCity("Entenhausen");
		testApplication.setCustomerFirstName("Donald");
		testApplication.setCustomerLastName("Duck");
		testApplication.setCustomerPostalCode("88888");
		testApplication.setCustomerStreet("Hauptstrasse");
		testApplication.setLazinessProtection(false);
		testApplication.setRetirementProtection(true);
		pres.setContractApplication(testApplication);
		pres.startPresenting();
		verify(view).setObserver(pres);
		verify(view).initializeUi();
		verify(view).setCustomerCity("Entenhausen");
		verify(view).setCustomerFirstName("Donald");
		verify(view).setCustomerLastName("Duck");
		verify(view).setCustomerPostalCode("88888");
		verify(view).setCustomerStreet("Hauptstrasse");
		verify(view).setLazinessProtection(false);
		verify(view).setRetirementProtection(true);
		verify(view).setMonthlyFeeStr("11.11");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnSave() {
		pres.startPresenting();
		assertNull(pres.getContractApplication().getCustomerCity());
		assertNull(pres.getContractApplication().getCustomerFirstName());
		assertNull(pres.getContractApplication().getCustomerLastName());
		assertNull(pres.getContractApplication().getCustomerPostalCode());
		assertNull(pres.getContractApplication().getCustomerStreet());
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertFalse(pres.getContractApplication().isRetirementProtection());
		when(view.getCustomerCity()).thenReturn("Springfield");
		when(view.getCustomerFirstName()).thenReturn("Marge");
		when(view.getCustomerLastName()).thenReturn("Simpson");
		when(view.getCustomerPostalCode()).thenReturn("12345");
		when(view.getCustomerStreet()).thenReturn("Evergreen Terrace");
		when(view.isLazinessProtection()).thenReturn(false);
		when(view.isRetirementProtection()).thenReturn(true);
		when(view.checkAllFieldsValid()).thenReturn(true);
		UnsupportedOperationException exc = null;
		try {
			pres.onSave();
		} catch (UnsupportedOperationException e) {
			exc = e;
		}
		assertNotNull(exc);
		assertNull(pres.getContractApplication().getCustomerCity());
		assertNull(pres.getContractApplication().getCustomerFirstName());
		assertNull(pres.getContractApplication().getCustomerLastName());
		assertNull(pres.getContractApplication().getCustomerPostalCode());
		assertNull(pres.getContractApplication().getCustomerStreet());
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertFalse(pres.getContractApplication().isRetirementProtection());
		verify(service, never()).addNewContractApplication(eq(pres.getContractApplication()), anyMap());
		verify(returnPres, never()).returnToThisPresener((Presenter) any());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnCancel() {
		pres.startPresenting();
		assertNull(pres.getContractApplication().getCustomerCity());
		assertNull(pres.getContractApplication().getCustomerFirstName());
		assertNull(pres.getContractApplication().getCustomerLastName());
		assertNull(pres.getContractApplication().getCustomerPostalCode());
		assertNull(pres.getContractApplication().getCustomerStreet());
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertFalse(pres.getContractApplication().isRetirementProtection());
		when(view.getCustomerCity()).thenReturn("Springfield");
		when(view.getCustomerFirstName()).thenReturn("Marge");
		when(view.getCustomerLastName()).thenReturn("Simpson");
		when(view.getCustomerPostalCode()).thenReturn("12345");
		when(view.getCustomerStreet()).thenReturn("Evergreen Terrace");
		when(view.isLazinessProtection()).thenReturn(false);
		when(view.isRetirementProtection()).thenReturn(true);
		when(view.checkAllFieldsValid()).thenReturn(true);
		pres.onCancel();
		assertNull(pres.getContractApplication().getCustomerCity());
		assertNull(pres.getContractApplication().getCustomerFirstName());
		assertNull(pres.getContractApplication().getCustomerLastName());
		assertNull(pres.getContractApplication().getCustomerPostalCode());
		assertNull(pres.getContractApplication().getCustomerStreet());
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertFalse(pres.getContractApplication().isRetirementProtection());
		verify(service, never()).addNewContractApplication(eq(pres.getContractApplication()), anyMap());
		verify(returnPres).returnToThisPresener((Presenter) any());
	}

}
