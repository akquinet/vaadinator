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
 */
package org.vergien.vaadinator.webdriver.touchkit;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.vaadin.addonhelpers.AbstractTest;
import org.vergien.vaadinator.webdriver.touchkit.dao.AddressDaoPlain;
import org.vergien.vaadinator.webdriver.touchkit.service.AddressService;
import org.vergien.vaadinator.webdriver.touchkit.service.AddressServicePlain;
import org.vergien.vaadinator.webdriver.touchkit.ui.presenter.Presenter;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.presenter.PresenterFactory;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.VaadinViewFactory;
import org.vergien.vaadinator.webdriver.touchkit.ui.view.View;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
@Theme("touchkitex")
public abstract class AbstractDemo extends AbstractTest {

	PresenterFactory presenterFactory = null;

	abstract Presenter getPresenter();

	@Override
	public Component getTestComponent() {
		NavigationManager navigationManager = new NavigationManager();
		navigationManager.setMaintainBreadcrumb(true);
		Presenter presenter;
		presenter = getPresenter();
		View view = presenter.getView();
		
		navigationManager.setCurrentComponent((Component) view.getComponent());
		
		presenter.startPresenting();

		return navigationManager;
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
