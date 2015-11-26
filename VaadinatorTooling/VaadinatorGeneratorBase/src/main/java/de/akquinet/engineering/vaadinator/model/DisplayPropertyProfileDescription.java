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
package de.akquinet.engineering.vaadinator.model;

import de.akquinet.engineering.vaadinator.annotations.FieldType;

public class DisplayPropertyProfileDescription {

	public DisplayPropertyProfileDescription() {
		super();
	}

	public DisplayPropertyProfileDescription(PropertyDescription property, String profileName) {
		super();
		this.property = property;
		this.profileName = profileName;
	}

	private PropertyDescription property;
	private String profileName;
	private boolean excluded = false;
	private boolean included = false;
	private FieldType fieldType = FieldType.TEXTFIELD;
	private String customClassName = null;
	private boolean customAuswahlAusListe = false;
	private boolean customUnboxed = false;
	private int order = 0;
	private boolean showInTable = false;
	private boolean showInDetail = true;
	private String sectionName = "Basisdaten";
	private float tableExpandRatio = 1.0f;
	private boolean readOnly = false;
	private boolean required = false;
	

	public PropertyDescription getProperty() {
		return property;
	}

	public void setProperty(PropertyDescription property) {
		this.property = property;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public DisplayProfileDescription getProfile() {
		if (getProperty() == null) {
			return null;
		}
		if (getProperty().getBean() == null) {
			return null;
		}
		return getProperty().getBean().getDisplayProfile(getProfileName());
	}

	public boolean isExcluded() {
		return excluded;
	}

	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}

	public boolean isIncluded() {
		return included;
	}

	public void setIncluded(boolean included) {
		this.included = included;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldTypeClassName() {
		if (FieldType.CUSTOM.equals(getFieldType())) {
			return getCustomClassName();
		}
		return getFieldType() == null ? null : getFieldType().getVaadinClassName();
	}

	public boolean isFieldTypeAuswahlAusListe() {
		if (FieldType.CUSTOM.equals(getFieldType())) {
			return isCustomAuswahlAusListe();
		}
		return getFieldType() == null ? false : getFieldType().isAuswahlAusListe();
	}

	public boolean isFieldTypeUnboxed() {
		if (FieldType.CUSTOM.equals(getFieldType())) {
			return isCustomUnboxed();
		}
		return getFieldType() == null ? false : getFieldType().isUnboxed();
	}

	public String getCustomClassName() {
		return customClassName;
	}

	public void setCustomClassName(String customClassName) {
		this.customClassName = customClassName;
	}
	
	public boolean isCustomAuswahlAusListe() {
		return customAuswahlAusListe;
	}

	public void setCustomAuswahlAusListe(boolean customAuswahlAusListe) {
		this.customAuswahlAusListe = customAuswahlAusListe;
	}

	public boolean isCustomUnboxed() {
		return customUnboxed;
	}

	public void setCustomUnboxed(boolean customUnboxed) {
		this.customUnboxed = customUnboxed;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isShowInTable() {
		return showInTable;
	}

	public void setShowInTable(boolean showInTable) {
		this.showInTable = showInTable;
	}

	public boolean isShowInDetail() {
		return showInDetail;
	}

	public void setShowInDetail(boolean showInDetail) {
		this.showInDetail = showInDetail;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public SectionDescription getSection() {
		if (getSectionName() == null) {
			return null;
		}
		return new SectionDescription(getSectionName());
	}

	public float getTableExpandRatio() {
		return tableExpandRatio;
	}

	public void setTableExpandRatio(float tableExpandRatio) {
		this.tableExpandRatio = tableExpandRatio;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public String toString() {
		return "DisplayPropertyProfileDescription [profileName=" + profileName + ", excluded=" + excluded + ", included=" + included + ", fieldType="
				+ fieldType + ", customClassName=" + customClassName + ", customAuswahlAusListe=" + customAuswahlAusListe + ", customUnboxed="
				+ customUnboxed + ", order=" + order + ", showInTable=" + showInTable + ", showInDetail=" + showInDetail + ", sectionName="
				+ sectionName + ", tableExpandRatio=" + tableExpandRatio + ", readOnly=" + readOnly + ", required=" + required
				+ ", getFieldTypeClassName()=" + getFieldTypeClassName() + ", isFieldTypeAuswahlAusListe()=" + isFieldTypeAuswahlAusListe() + "]";
	}

}
