/*
 * Copyright 2016 Daniel Norhoff-Vergien
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
 */package org.vergien.vaadinator.webdriver.touchkit;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.vaadin.addonhelpers.AbstractTest;
import org.vergien.vaadinator.webdriver.touchkit.dao.AddressDaoPlain;
import org.vergien.vaadinator.webdriver.touchkit.service.AddressService;
import org.vergien.vaadinator.webdriver.touchkit.service.AddressServicePlain;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.presenter.FirstPagePresenter;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.presenter.PresenterFactory;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.FirstPageView;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.VaadinViewFactory;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.ui.Component;

@Title("WebDriverExampleTouchkit")
@Theme("touchkitex")
public class WebDriverExampleDemo extends AbstractTest {

	PresenterFactory presenterFactory = null;

	@Override
	public Component getTestComponent() {
		NavigationManager m = new NavigationManager();
		m.setMaintainBreadcrumb(true);
		FirstPagePresenter fpres;
		fpres = obtainPresenterFactory().createFirstPagePresenter();
		FirstPageView fview = fpres.getView();
		m.setCurrentComponent((Component) fview.getComponent());
		// setContent(m);
		// and go
		fpres.startPresenting();

		return m;
	}

	protected PresenterFactory obtainPresenterFactory() {
		if (presenterFactory == null) {
			AddressService addressService;
			EntityManagerFactory entityManagerFactory = Persistence
					.createEntityManagerFactory("WebDriverExampleTouchkit");
			AddressDaoPlain addressDaoPlain = new AddressDaoPlain(entityManagerFactory);
			addressService = new AddressServicePlain(entityManagerFactory, addressDaoPlain);

			presenterFactory = new PresenterFactory(new HashMap<String, Object>(), new VaadinViewFactory(),
					addressService);
		}
		return presenterFactory;
	}
}
