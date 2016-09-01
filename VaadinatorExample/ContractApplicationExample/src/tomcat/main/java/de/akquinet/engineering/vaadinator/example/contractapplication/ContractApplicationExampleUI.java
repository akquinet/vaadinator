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
package de.akquinet.engineering.vaadinator.example.contractapplication;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import de.akquinet.engineering.vaadinator.example.contractapplication.dao.ContractApplicationDao;
import de.akquinet.engineering.vaadinator.example.contractapplication.dao.ContractApplicationDaoPlain;
import de.akquinet.engineering.vaadinator.example.contractapplication.service.ContractApplicationService;
import de.akquinet.engineering.vaadinator.example.contractapplication.service.ContractApplicationServicePlain;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.presenter.FirstPagePresenter;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.presenter.PresenterFactory;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.FirstPageView;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.VaadinViewFactoryEx;

/**
 * Main UI class
 */
@Title("ContractApplicationExample")
@Theme("touchkitex")
@Widgetset("de.akquinet.engineering.vaadinator.example.contractapplication.MobileWidgetset")
public class ContractApplicationExampleUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		NavigationManager m = new NavigationManager();
		m.setMaintainBreadcrumb(true);
		FirstPagePresenter fpres;
		fpres = obtainPresenterFactory(request.getContextPath()).createFirstPagePresenter();
		FirstPageView fview = (FirstPageView) fpres.getView();
		m.setCurrentComponent((Component) fview.getComponent());
		setContent(m);
		// and go
		fpres.startPresenting();
	}

	PresenterFactory presenterFactory = null;

	protected PresenterFactory obtainPresenterFactory(String contextPath) {
		if (presenterFactory == null) {
			ContractApplicationService contractApplicationService;
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ContractApplicationExample");
			ContractApplicationDao contractApplicationDao = new ContractApplicationDaoPlain(entityManagerFactory);
			contractApplicationService = new ContractApplicationServicePlain(entityManagerFactory, contractApplicationDao);
			presenterFactory = new PresenterFactory(new HashMap<String, Object>(), new VaadinViewFactoryEx(), contractApplicationService);
		}
		return presenterFactory;
	}

}