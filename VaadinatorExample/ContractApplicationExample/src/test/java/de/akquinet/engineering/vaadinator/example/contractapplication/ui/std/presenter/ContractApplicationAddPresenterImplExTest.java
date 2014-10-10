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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
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
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.ContractApplicationAddView;

public class ContractApplicationAddPresenterImplExTest {

	ContractApplicationAddView view;
	Presenter returnPres;
	ContractApplicationService service;
	ContractApplicationAddPresenterImplEx pres;

	@Before
	public void setUp() {
		view = mock(ContractApplicationAddView.class);
		returnPres = mock(Presenter.class);
		service = mock(ContractApplicationService.class);
		pres = new ContractApplicationAddPresenterImplEx(new HashMap<String, Object>(), view, returnPres, service);
	}

	@Test
	public void testStartPresenting() {
		ContractApplication noiseApplication = new ContractApplication();
		noiseApplication.setCustomerCity("Entenhausen");
		noiseApplication.setCustomerFirstName("Donald");
		noiseApplication.setCustomerLastName("Duck");
		noiseApplication.setCustomerPostalCode("88888");
		noiseApplication.setCustomerStreet("Hauptstrasse");
		noiseApplication.setLazinessProtection(false);
		noiseApplication.setRetirementProtection(true);
		pres.setContractApplication(noiseApplication);
		pres.startPresenting();
		verify(view).setObserver(pres);
		verify(view).initializeUi();
		verify(view).setCustomerCity(null);
		verify(view).setCustomerFirstName(null);
		verify(view).setCustomerLastName(null);
		verify(view).setCustomerPostalCode(null);
		verify(view).setCustomerStreet(null);
		verify(view).setLazinessProtection(true);
		verify(view).setRetirementProtection(false);
		verify(view).setMonthlyFeeStr("88.88");
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
		pres.onSave();
		assertEquals("Springfield", pres.getContractApplication().getCustomerCity());
		assertEquals("Marge", pres.getContractApplication().getCustomerFirstName());
		assertEquals("Simpson", pres.getContractApplication().getCustomerLastName());
		assertEquals("12345", pres.getContractApplication().getCustomerPostalCode());
		assertEquals("Evergreen Terrace", pres.getContractApplication().getCustomerStreet());
		assertFalse(pres.getContractApplication().isLazinessProtection());
		assertTrue(pres.getContractApplication().isRetirementProtection());
		verify(service).addNewContractApplication(eq(pres.getContractApplication()), anyMap());
		verify(returnPres).returnToThisPresener((Presenter) any());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnSaveInvalid() {
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
		when(view.checkAllFieldsValid()).thenReturn(false);
		pres.onSave();
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
	public void testOnSaveException() {
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
		when(service.addNewContractApplication((ContractApplication) any(), anyMap())).thenThrow(new RuntimeException("Exception test"));
		try {
			pres.onSave();
		} catch (RuntimeException rte) {
		}
		verify(view).showErrorMessage("java.lang.RuntimeException: Exception test");
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

	@Test
	public void testOnProductChoiceModified() {
		pres.startPresenting();
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertFalse(pres.getContractApplication().isRetirementProtection());
		when(view.isLazinessProtection()).thenReturn(false);
		when(view.isRetirementProtection()).thenReturn(true);
		when(view.checkAllFieldsValid()).thenReturn(true);
		pres.onProductChoiceModified();
		assertFalse(pres.getContractApplication().isLazinessProtection());
		assertTrue(pres.getContractApplication().isRetirementProtection());
		verify(view).setMonthlyFeeStr("11.11");
	}

	@Test
	public void testOnProductChoiceModifiedAll() {
		pres.startPresenting();
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertFalse(pres.getContractApplication().isRetirementProtection());
		when(view.isLazinessProtection()).thenReturn(true);
		when(view.isRetirementProtection()).thenReturn(true);
		when(view.checkAllFieldsValid()).thenReturn(true);
		pres.onProductChoiceModified();
		assertTrue(pres.getContractApplication().isLazinessProtection());
		assertTrue(pres.getContractApplication().isRetirementProtection());
		verify(view).setMonthlyFeeStr("99.99");
	}

}
