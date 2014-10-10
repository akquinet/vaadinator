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
package de.akquinet.engineering.vaadinator.example.contractapplication.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.DisplayPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.FieldType;

@DisplayBean(captionText = "Antrag")
@Entity
@Access(AccessType.FIELD)
public class ContractApplication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractApplication() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@DisplayProperty(captionText = "Vorname", profileSettings = { @DisplayPropertySetting(sectionName = "Ihre Person", required = true) })
	private String customerFirstName;
	@DisplayProperty(captionText = "Nachname", profileSettings = { @DisplayPropertySetting(sectionName = "Ihre Person", required = true) })
	private String customerLastName;
	@DisplayProperty(captionText = "Straße", profileSettings = { @DisplayPropertySetting(sectionName = "Ihre Person") })
	private String customerStreet;
	@DisplayProperty(captionText = "PLZ", profileSettings = { @DisplayPropertySetting(sectionName = "Ihre Person") })
	private String customerPostalCode;
	@DisplayProperty(captionText = "Ort", profileSettings = { @DisplayPropertySetting(sectionName = "Ihre Person") })
	private String customerCity;

	@DisplayProperty(captionText = "Altersvorsorge", profileSettings = { @DisplayPropertySetting(sectionName = "Ihr Vorsorgewunsch", fieldType = FieldType.CHECKBOX) })
	private boolean retirementProtection = false;
	@DisplayProperty(captionText = "Keine-Lust-Vorsorge", profileSettings = { @DisplayPropertySetting(sectionName = "Ihr Vorsorgewunsch", fieldType = FieldType.CHECKBOX) })
	private boolean lazinessProtection = true;

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	@DisplayProperty(captionText = "Name", profileSettings = { @DisplayPropertySetting(readOnly = true, showInDetail = false, showInTable = true) })
	public String getCustomerName() {
		return getCustomerFirstName() + " " + getCustomerLastName();
	}

	public String getCustomerStreet() {
		return customerStreet;
	}

	public void setCustomerStreet(String customerStreet) {
		this.customerStreet = customerStreet;
	}

	public String getCustomerPostalCode() {
		return customerPostalCode;
	}

	public void setCustomerPostalCode(String customerPostalCode) {
		this.customerPostalCode = customerPostalCode;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public boolean isRetirementProtection() {
		return retirementProtection;
	}

	public void setRetirementProtection(boolean retirementProtection) {
		this.retirementProtection = retirementProtection;
	}
	
	@DisplayProperty(captionText = "Alter", profileSettings = { @DisplayPropertySetting(readOnly = true, showInDetail = false, showInTable = true) })
	public String getRetirementProtectionStr() {
		return isRetirementProtection() ? "Ja" : "Nein";
	}

	public boolean isLazinessProtection() {
		return lazinessProtection;
	}

	public void setLazinessProtection(boolean lazinessProtection) {
		this.lazinessProtection = lazinessProtection;
	}

	@DisplayProperty(captionText = "Keine-Lust", profileSettings = { @DisplayPropertySetting(readOnly = true, showInDetail = false, showInTable = true) })
	public String getLazinessProtectionStr() {
		return isLazinessProtection() ? "Ja" : "Nein";
	}

	@Access(AccessType.PROPERTY)
	public int getMonthlyFee() {
		// (we want to have it in the DB also so we can query - see below)
		return 0 + (isRetirementProtection() ? 1111 : 0) + (isLazinessProtection() ? 8888 : 0);
	}

	public void setMonthlyFee(int monthlyFee) {
		// calc field (btw a general receipe): have pseudo-setter to calm JPA,
		// but don't do anything here. We want to have it in the DB to be able
		// to query (poss. a superclass / subclass can also write it! - but in
		// this case it is calculated and should be that way - written upon
		// persisting the entity)
	}

	@DisplayProperty(captionText = "Monatsbeitrag", profileSettings = { @DisplayPropertySetting(sectionName = "Kalkulation", fieldType = FieldType.LABEL, readOnly = true) })
	public String getMonthlyFeeStr() {
		return "" + (getMonthlyFee() / 100.0f);
	}

	@Override
	public String toString() {
		return "ContractApplication [customerFirstName=" + customerFirstName + ", customerLastName=" + customerLastName + ", customerStreet="
				+ customerStreet + ", customerPostalCode=" + customerPostalCode + ", customerCity=" + customerCity + ", retirementProtection="
				+ retirementProtection + ", lazinessProtection=" + lazinessProtection + ", getMonthlyFee()=" + getMonthlyFee()
				+ ", getMonthlyFeeStr()=" + getMonthlyFeeStr() + "]";
	}

}
