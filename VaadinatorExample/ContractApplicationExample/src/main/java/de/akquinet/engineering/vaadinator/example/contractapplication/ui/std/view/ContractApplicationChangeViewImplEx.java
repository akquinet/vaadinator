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
package de.akquinet.engineering.vaadinator.example.contractapplication.ui.std.view;


public class ContractApplicationChangeViewImplEx extends ContractApplicationChangeViewImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractApplicationChangeViewImplEx() {
		super();
	}

	@Override
	public void initializeUi() {
		super.initializeUi();
		// beauty OP
		setCaption("Abgeschickter Antrag");
		cancel.setCaption("Schließen");
		// don't allow changing once-submitted stuff
		// (which is only in the ChangeView)
		customerCity.setEnabled(false);
		customerFirstName.setEnabled(false);
		customerLastName.setEnabled(false);
		customerPostalCode.setEnabled(false);
		customerStreet.setEnabled(false);
		lazinessProtection.setEnabled(false);
		retirementProtection.setEnabled(false);
		// also hide save
		save.setVisible(false);
		// also reshuffle sections
		layout.removeComponent(sectionKalkulation);
		layout.addComponent(sectionKalkulation, layout.getComponentIndex(sectionIhrVorsorgewunsch) + 1);
		sectionBasisdaten.setVisible(false);
	}

}
