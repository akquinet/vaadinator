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
package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.Anreden;
import de.akquinet.engineering.vaadinator.example.address.model.Filme;
import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressAddView;

public class AddressAddPresenterImplTest {

	private AddressAddView view;
	private Presenter returnPres;
	private AddressService service;
	private AddressAddPresenterImpl pres;

	@Before
	public void setUp() {
		view = mock(AddressAddView.class);
		when(view.getNachname()).thenReturn("nachname");
		when(view.getVorname()).thenReturn("vorname");
		when(view.getAnrede()).thenReturn(Anreden.FRAU);
		when(view.getEmail()).thenReturn("email");
		when(view.getGeburtsdatum()).thenReturn(new Date(0));
		returnPres = mock(Presenter.class);
		service = mock(AddressService.class);
		pres = new AddressAddPresenterImpl(new HashMap<String, Object>(), view, returnPres, service);
	}

	@Test
	public void testStartPresenting() {
		pres.setAddress(new Address(Anreden.FROLLEIN, "Sabine", "Test", "st@test.com"));
		pres.startPresenting();
		verify(view).setObserver(pres);
		verify(view).initializeUi();
		verify(view).setChoicesForAnrede(argThat(new BaseMatcher<List<Anreden>>() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object arg0) {
				for (Anreden a : Anreden.values()) {
					if (!((List<Anreden>) arg0).contains(a)) {
						return false;
					}
				}
				return true;
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("Contains all Anreden");
			}
		}));
		verify(view).setChoicesForMagFilme(argThat(new BaseMatcher<List<Filme>>() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object arg0) {
				for (Filme f : Filme.values()) {
					if (!((List<Filme>) arg0).contains(f)) {
						return false;
					}
				}
				return true;
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("Contains all Anreden");
			}
		}));
		verify(view).setNachname(null);
		verify(view).setVorname(null);
		verify(view).setAnrede(null);
		verify(view).setEmail(null);
		verify(view).setGeburtsdatum(null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnSave() {
		pres.startPresenting();
		assertNull(pres.getAddress().getAnrede());
		assertNull(pres.getAddress().getNachname());
		assertNull(pres.getAddress().getVorname());
		assertNull(pres.getAddress().getEmail());
		assertNull(pres.getAddress().getGeburtsdatum());
		when(view.getAnrede()).thenReturn(Anreden.FROLLEIN);
		when(view.getNachname()).thenReturn("Test");
		when(view.getVorname()).thenReturn("Sabine");
		when(view.getEmail()).thenReturn("st@test.com");
		Date gebdate = new Date();
		when(view.getGeburtsdatum()).thenReturn(gebdate);
		when(view.checkAllFieldsValid()).thenReturn(true);
		pres.onSave();
		assertEquals(Anreden.FROLLEIN, pres.getAddress().getAnrede());
		assertEquals("Test", pres.getAddress().getNachname());
		assertEquals("Sabine", pres.getAddress().getVorname());
		assertEquals("st@test.com", pres.getAddress().getEmail());
		assertEquals(gebdate, pres.getAddress().getGeburtsdatum());
		verify(service).addNewAddress(eq(pres.getAddress()), anyMap());
		verify(returnPres).returnToThisPresener((Presenter) any());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnSaveInvalid() {
		pres.startPresenting();
		assertNull(pres.getAddress().getAnrede());
		assertNull(pres.getAddress().getNachname());
		assertNull(pres.getAddress().getVorname());
		assertNull(pres.getAddress().getEmail());
		assertNull(pres.getAddress().getGeburtsdatum());
		when(view.getAnrede()).thenReturn(Anreden.FROLLEIN);
		when(view.getNachname()).thenReturn("Test");
		when(view.getVorname()).thenReturn("Sabine");
		when(view.getEmail()).thenReturn("st@test.com");
		Date gebdate = new Date();
		when(view.getGeburtsdatum()).thenReturn(gebdate);
		when(view.checkAllFieldsValid()).thenReturn(false);
		pres.onSave();
		assertNull(pres.getAddress().getAnrede());
		assertNull(pres.getAddress().getNachname());
		assertNull(pres.getAddress().getVorname());
		assertNull(pres.getAddress().getEmail());
		assertNull(pres.getAddress().getGeburtsdatum());
		verify(service, never()).addNewAddress(eq(pres.getAddress()), anyMap());
		verify(returnPres, never()).returnToThisPresener((Presenter) any());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOnCancel() {
		pres.startPresenting();
		assertNull(pres.getAddress().getAnrede());
		assertNull(pres.getAddress().getNachname());
		assertNull(pres.getAddress().getVorname());
		assertNull(pres.getAddress().getEmail());
		assertNull(pres.getAddress().getGeburtsdatum());
		when(view.getAnrede()).thenReturn(Anreden.FROLLEIN);
		when(view.getNachname()).thenReturn("Test");
		when(view.getVorname()).thenReturn("Sabine");
		when(view.getEmail()).thenReturn("st@test.com");
		Date gebdate = new Date();
		when(view.getGeburtsdatum()).thenReturn(gebdate);
		when(view.checkAllFieldsValid()).thenReturn(true);
		pres.onCancel();
		assertNull(pres.getAddress().getAnrede());
		assertNull(pres.getAddress().getNachname());
		assertNull(pres.getAddress().getVorname());
		assertNull(pres.getAddress().getEmail());
		assertNull(pres.getAddress().getGeburtsdatum());
		verify(service, never()).addNewAddress(eq(pres.getAddress()), anyMap());
		verify(returnPres).returnToThisPresener((Presenter) any());
	}

}
