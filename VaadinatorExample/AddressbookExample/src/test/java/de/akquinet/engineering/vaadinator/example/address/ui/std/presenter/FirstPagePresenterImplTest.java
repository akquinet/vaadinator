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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.model.Anreden;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressAddView;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressListView;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.FirstPageView;

public class FirstPagePresenterImplTest {

	private FirstPageView view;
	private AddressListView lview;
	private AddressAddView aview;
	private PresenterFactory presenterFactory;
	private AddressListPresenter lpres;
	private AddressAddPresenter apres;
	private FirstPagePresenterImpl pres;

	@Before
	public void setUp() {
		view = mock(FirstPageView.class);
		lview = mock(AddressListView.class);
		aview = mock(AddressAddView.class);
		when(aview.getNachname()).thenReturn("nachname");
		when(aview.getVorname()).thenReturn("vorname");
		when(aview.getAnrede()).thenReturn(Anreden.FRAU);
		when(aview.getEmail()).thenReturn("email");
		when(aview.getGeburtsdatum()).thenReturn(new Date(0));
		presenterFactory = mock(PresenterFactory.class);
		lpres = mock(AddressListPresenterImpl.class);
		when(lpres.getView()).thenReturn(lview);
		apres = mock(AddressAddPresenterImpl.class);
		when(apres.getView()).thenReturn(aview);
		when(presenterFactory.createAddressListPresenter((Presenter) any(), (SubviewCapablePresenter) any())).thenReturn(lpres);
		when(presenterFactory.createAddressAddPresenter((Presenter) any())).thenReturn(apres);
		pres = new FirstPagePresenterImpl(new HashMap<String, Object>(), view, presenterFactory);
	}

	@Test
	public void testReturnToThisPresener() {
		pres.returnToThisPresener(mock(Presenter.class));
		verify(view).closeSubView();
	}

	@Test
	public void testStartPresenting() {
		pres.startPresenting();
		verify(view).setObserver(pres);
		verify(view).initializeUi();
	}

	@Test
	public void testOnListAddresses() {
		pres.onListAddress();
		verify(presenterFactory).createAddressListPresenter(eq(pres), (SubviewCapablePresenter) isNull());
		verify(view).openSubView(lview);
		verify(lpres).startPresenting();
	}

	@Test
	public void testOnAddAddress() {
		pres.onAddAddress();
		verify(presenterFactory).createAddressAddPresenter(pres);
		verify(view).openSubView(aview);
		verify(apres).startPresenting();
	}

}
