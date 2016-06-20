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
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.example.address.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressListView;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.MainView;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.TeamListView;

public class MainPresenterImplTest {

	MainView view;
	AddressListView lview;
	PresenterFactory presenterFactory;
	AddressListPresenter lpres;
	
	TeamListView tview;
	TeamListPresenter tpres;
	
	MainPresenterImpl pres;

	@Before
	public void setUp() {
		view = mock(MainView.class);
		lview = mock(AddressListView.class);
		presenterFactory = mock(PresenterFactory.class);
		lpres = mock(AddressListPresenterImpl.class);
		tview = mock(TeamListView.class);
		tpres = mock(TeamListPresenterImpl.class);
		when(lpres.getView()).thenReturn(lview);
		when(presenterFactory.createAddressListPresenter((Presenter) any(), (SubviewCapablePresenter) any())).thenReturn(lpres);
		when(tpres.getView()).thenReturn(tview);
		when(presenterFactory.createTeamListPresenter((Presenter) any(), (SubviewCapablePresenter) any())).thenReturn(tpres);
		
		pres = new MainPresenterImpl(new HashMap<String, Object>(), view, presenterFactory);
	}

	@Test
	public void testStartPresenting() {
		pres.startPresenting();
		verify(presenterFactory).createAddressListPresenter(eq(pres), (SubviewCapablePresenter) isNotNull());
		verify(view).setMasterView("Address", lview);
		verify(lpres).startPresenting();
	}

}
