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

import java.util.Map;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.contractapplication.ui.view.ExceptionMappingStrategy;


public class ContractApplicationChangeViewImplEx extends ContractApplicationChangeViewImpl implements EagerValidatableView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractApplicationChangeViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy) {
		super(exceptionMappingStrategy);
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
	
	@Override
	public void onValidationError(Map<Field<?>, InvalidValueException> validationErrors) {
		super.onValidationError(validationErrors);
		StringBuilder sb = new StringBuilder();
		for(Field<?> field: validationErrors.keySet()) {
			if(sb.length()!= 0) {
				sb.append(", ");
			}
			sb.append(field.getCaption());		
		}
		Notification.show("Fehler in folgenden Feldern:", sb.toString(), Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		System.out.println(event);
		
	}

	@Override
	public void textChange(TextChangeEvent event) {
		System.out.println(event);
		
	}
}
