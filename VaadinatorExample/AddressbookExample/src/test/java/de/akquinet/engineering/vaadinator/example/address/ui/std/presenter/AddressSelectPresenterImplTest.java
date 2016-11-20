/*
 * Copyright 2016 Daniel Nordhoff-Vergien
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

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.Anreden;
import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressSelectView;

public class AddressSelectPresenterImplTest {
	private AddressSelectView view;
	private AddressService service;
	private AddressSelectPresenterImpl pres;
	private AddressSelectPresenter.Observer observer;

	@Before
	public void setUp() {
		view = mock(AddressSelectView.class);
		when(view.getAddressSelection()).thenReturn(mock(Address.class));
		service = mock(AddressService.class);
		observer = mock(AddressSelectPresenter.Observer.class);
		pres = new AddressSelectPresenterImpl(new HashMap<String, Object>(), view, service);
		pres.setObserver(observer);
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

	@Test
	public void testOnAddressChoosen() {
		Address address = new Address(Anreden.FROLLEIN, "Sabine", "Test", "st@test.com");
		when(view.getAddressSelection()).thenReturn(address);
		pres.onAddressChosen();
		verify(view).getAddressSelection();
		verify(observer).onAddressSelected(address);
	}
}
