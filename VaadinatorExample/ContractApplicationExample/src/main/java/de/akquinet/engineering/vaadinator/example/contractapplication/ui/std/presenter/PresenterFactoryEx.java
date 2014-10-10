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

import java.util.Map;

import de.akquinet.engineering.vaadinator.example.contractapplication.service.ContractApplicationService;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.presenter.Presenter;
import de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view.ViewFactory;

public class PresenterFactoryEx extends PresenterFactory {

	public PresenterFactoryEx(Map<String, Object> context, ViewFactory viewFactory, ContractApplicationService contractApplicationService) {
		super(context, viewFactory, contractApplicationService);
		this.context = context;
		this.viewFactory = viewFactory;
		this.contractApplicationService = contractApplicationService;
	}

	private Map<String, Object> context;
	private ViewFactory viewFactory;
	private ContractApplicationService contractApplicationService;

	@Override
	public ContractApplicationAddPresenter createContractApplicationAddPresenter(Presenter returnPresenter) {
		return new ContractApplicationAddPresenterImplEx(context, viewFactory.createContractApplicationAddView(), returnPresenter,
				contractApplicationService);
	}

	@Override
	public ContractApplicationChangePresenter createContractApplicationChangePresenter(Presenter returnPresenter) {
		return new ContractApplicationChangePresenterImplEx(context, viewFactory.createContractApplicationChangeView(), returnPresenter,
				contractApplicationService);
	}

}
