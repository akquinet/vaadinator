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
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.contractapplication.ui.view.ExceptionMappingStrategy;

public class ContractApplicationAddViewImplEx extends ContractApplicationAddViewImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractApplicationAddViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy) {
		super(exceptionMappingStrategy);
	}

	@Override
	public void initializeUi() {
		super.initializeUi();
		// beauty OP
		setCaption("Neuer Antrag");
		// be reactive - when we have an Observer that can react to
		// onTickChange, use it (only for AddView)
		retirementProtection.setImmediate(true);
		retirementProtection.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (observer != null && observer instanceof ProductChoiceAwareObserver) {
					((ProductChoiceAwareObserver) observer).onProductChoiceModified();
				}
			}
		});
		lazinessProtection.setImmediate(true);
		lazinessProtection.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (observer != null && observer instanceof ProductChoiceAwareObserver) {
					((ProductChoiceAwareObserver) observer).onProductChoiceModified();
				}
			}
		});
		// also rename submit
		save.setCaption("Abschicken");
		// also reshuffle sections
		layout.removeComponent(sectionKalkulation);
		layout.addComponent(sectionKalkulation, layout.getComponentIndex(sectionIhrVorsorgewunsch) + 1);
		sectionBasisdaten.setVisible(false);
	}

	private ContractApplicationAddView.Observer observer = null;

	@Override
	public void setObserver(ContractApplicationAddView.Observer observer) {
		super.setObserver(observer);
		this.observer = observer;
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
}