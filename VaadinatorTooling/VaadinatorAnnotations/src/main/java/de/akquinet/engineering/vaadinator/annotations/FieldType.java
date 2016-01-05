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
package de.akquinet.engineering.vaadinator.annotations;

public enum FieldType {

	TEXTFIELD("com.vaadin.ui.TextField"), TEXTAREA("com.vaadin.ui.TextArea"), LABEL("com.vaadin.ui.Label"), DROPDOWN("com.vaadin.ui.ComboBox", true), DATEPICKER(
			"com.vaadin.addon.touchkit.ui.DatePicker"), CHECKBOX("com.vaadin.ui.CheckBox", false, true), RADIO("com.vaadin.ui.OptionGroup", true), PASSWORD("com.vaadin.ui.PasswordField"), CUSTOM(
			null);

	private FieldType(String vaadinClassName) {
		this.vaadinClassName = vaadinClassName;
	}

	private FieldType(String vaadinClassName, boolean auswahlAusListe) {
		this.vaadinClassName = vaadinClassName;
		this.auswahlAusListe = auswahlAusListe;
	}

	private FieldType(String vaadinClassName, boolean auswahlAusListe, boolean unboxed) {
		this.vaadinClassName = vaadinClassName;
		this.auswahlAusListe = auswahlAusListe;
		this.unboxed = unboxed;
	}

	private String vaadinClassName;
	private boolean auswahlAusListe = false;
	private boolean unboxed = false;

	public String getVaadinClassName() {
		return vaadinClassName;
	}

	public boolean isAuswahlAusListe() {
		return auswahlAusListe;
	}

	public boolean isUnboxed() {
		return unboxed;
	}
}
