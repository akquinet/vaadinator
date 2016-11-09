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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.Anreden;
import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressAddView;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressChangeView;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressListView;

public class AddressListPresenterImplTest {

	AddressListView view;
	AddressChangeView cview;
	AddressAddView aview;
	AddressService service;
	PresenterFactory presenterFactory;
	AddressChangePresenter cpres;
	AddressAddPresenter apres;
	AddressListPresenterImpl pres;

	@Before
	public void setUp() {
		view = mock(AddressListView.class);
		when(view.getAddressSelection()).thenReturn(mock(Address.class));
		cview = mock(AddressChangeView.class);
		when(cview.getAnrede()).thenReturn(Anreden.FRAU);
		when(cview.getVorname()).thenReturn("vorname");
		when(cview.getNachname()).thenReturn("nachname");
		when(cview.getEmail()).thenReturn("email");
		when(cview.getGeburtsdatum()).thenReturn(new Date(0));
		aview = mock(AddressAddView.class);
		when(aview.getAnrede()).thenReturn(Anreden.FRAU);
		when(aview.getVorname()).thenReturn("vorname");
		when(aview.getNachname()).thenReturn("nachname");
		when(aview.getEmail()).thenReturn("email");
		when(aview.getGeburtsdatum()).thenReturn(new Date(0));
		service = mock(AddressService.class);
		presenterFactory = mock(PresenterFactory.class);
		cpres = mock(AddressChangePresenter.class);
		when(cpres.getView()).thenReturn(cview);
		apres = mock(AddressAddPresenter.class);
		when(apres.getView()).thenReturn(aview);
		when(presenterFactory.createAddressAddPresenter((Presenter) any())).thenReturn(apres);
		when(presenterFactory.createAddressChangePresenter((Presenter) any())).thenReturn(cpres);
		pres = new AddressListPresenterImpl(new HashMap<String, Object>(), view, presenterFactory, service, null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testStartPresenting() {
		pres.startPresenting();
		verify(view).setObserver(pres);
		verify(service).listAllAddress(anyMap());
		verify(view).initializeUi();
		verify(view).setOrRefreshData(anyList());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReturnToThisPresener() {
		pres.returnToThisPresener(mock(Presenter.class));
		verify(view).closeSubView();
		verify(view).setOrRefreshData(anyList());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReturnToThisPresenerSubview() {
		SubviewCapablePresenter subviewCapablePresenter = mock(SubviewCapablePresenter.class);
		pres = new AddressListPresenterImpl(new HashMap<String, Object>(), view, presenterFactory, service, subviewCapablePresenter);
		pres.returnToThisPresener(mock(Presenter.class));
		verify(view, never()).closeSubView();
		verify(subviewCapablePresenter).setDetailView(null);
		verify(view).setOrRefreshData(anyList());
	}

	@Test
	public void testOnAddressChosen() {
		when(view.getAddressSelection()).thenReturn(new Address(Anreden.FROLLEIN, "Sabine", "Test", "st@test.com"));
		pres.onAddressChosen();
		verify(presenterFactory).createAddressChangePresenter(pres);
		verify(view).openSubView(cview);
		verify(cpres).startPresenting();
	}

	@Test
	public void testOnAddressChosenSubview() {
		SubviewCapablePresenter subviewCapablePresenter = mock(SubviewCapablePresenter.class);
		pres = new AddressListPresenterImpl(new HashMap<String, Object>(), view, presenterFactory, service, subviewCapablePresenter);
		when(view.getAddressSelection()).thenReturn(new Address(Anreden.FROLLEIN, "Sabine", "Test", "st@test.com"));
		pres.onAddressChosen();
		verify(presenterFactory).createAddressChangePresenter(pres);
		verify(view, never()).openSubView(cview);
		verify(subviewCapablePresenter).setDetailView(cview);
		verify(cpres).startPresenting();
	}

	@Test
	public void testOnAddressChosenNull() {
		when(view.getAddressSelection()).thenReturn(null);
		pres.onAddressChosen();
		verify(presenterFactory, never()).createAddressChangePresenter((Presenter) any());
		verify(view, never()).openSubView(cview);
		verify(cpres, never()).startPresenting();
	}

	@Test
	public void testOnAddressChosenNullSubview() {
		SubviewCapablePresenter subviewCapablePresenter = mock(SubviewCapablePresenter.class);
		pres = new AddressListPresenterImpl(new HashMap<String, Object>(), view, presenterFactory, service, subviewCapablePresenter);
		when(view.getAddressSelection()).thenReturn(null);
		pres.onAddressChosen();
		verify(presenterFactory, never()).createAddressChangePresenter((Presenter) any());
		verify(view, never()).openSubView(cview);
		verify(subviewCapablePresenter, never()).setDetailView(cview);
		verify(cpres, never()).startPresenting();
	}

	@Test
	public void testOnAddAddress() {
		when(view.getAddressSelection()).thenReturn(new Address(Anreden.FROLLEIN, "Sabine", "Test", "st@test.com"));
		pres.onAddAddress();
		verify(presenterFactory).createAddressAddPresenter(pres);
		verify(view).openSubView(aview);
		verify(apres).startPresenting();
	}

	@Test
	public void testOnAddAddressSubview() {
		SubviewCapablePresenter subviewCapablePresenter = mock(SubviewCapablePresenter.class);
		pres = new AddressListPresenterImpl(new HashMap<String, Object>(), view, presenterFactory, service, subviewCapablePresenter);
		when(view.getAddressSelection()).thenReturn(new Address(Anreden.FROLLEIN, "Sabine", "Test", "st@test.com"));
		pres.onAddAddress();
		verify(presenterFactory).createAddressAddPresenter(pres);
		verify(view, never()).openSubView(aview);
		verify(subviewCapablePresenter).setDetailView(aview);
		verify(apres).startPresenting();
	}

}
