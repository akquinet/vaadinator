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

public class MapPropertyProfileDescription {

	public MapPropertyProfileDescription() {
		super();
	}

	public MapPropertyProfileDescription(PropertyDescription property, String profileName, String targetPropertyName) {
		super();
		this.property = property;
		this.profileName = profileName;
		this.targetPropertyName = targetPropertyName;
	}

	private PropertyDescription property;
	private String profileName;
	private boolean excluded = false;
	private boolean included = false;
	private String targetPropertyName;
	private String targetPropertyClassName = null;
	private boolean readonly = false;
	private boolean deep = false;
	private boolean mandatory = false;

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

	public MapProfileDescription getProfile() {
		if (getProperty() == null) {
			return null;
		}
		if (getProperty().getBean() == null) {
			return null;
		}
		return getProperty().getBean().getMapProfile(getProfileName());
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

	public String getTargetPropertyName() {
		return targetPropertyName;
	}

	public void setTargetPropertyName(String targetPropertyName) {
		this.targetPropertyName = targetPropertyName;
	}

	public String getTargetPropertyGetterName() {
		if (getTargetPropertyName() == null) {
			return null;
		}
		return "get" + getTargetPropertyName().substring(0, 1).toUpperCase() + getTargetPropertyName().substring(1);
	}

	public String getTargetPropertySetterName() {
		if (getTargetPropertyName() == null) {
			return null;
		}
		return "set" + getTargetPropertyName().substring(0, 1).toUpperCase() + getTargetPropertyName().substring(1);
	}

	public String getTargetPropertyClassName() {
		return targetPropertyClassName;
	}

	public void setTargetPropertyClassName(String targetPropertyClassName) {
		this.targetPropertyClassName = targetPropertyClassName;
	}

	public boolean isDeep() {
		return deep;
	}

	public void setDeep(boolean deep) {
		this.deep = deep;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	@Override
	public String toString() {
		return "MapPropertyProfileDescription [profileName=" + profileName + ", excluded=" + excluded + ", included=" + included
				+ ", targetPropertyName=" + targetPropertyName + ", targetPropertyClassName=" + targetPropertyClassName + ", deep=" + deep
				+ ", readonly=" + readonly + ", mandatory=" + mandatory + ", getTargetPropertyGetterName()=" + getTargetPropertyGetterName() + "]";
	}

}
