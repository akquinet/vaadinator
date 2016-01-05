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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.akquinet.engineering.vaadinator.annotations.Constants;

public class PropertyDescription {

	public PropertyDescription() {
		super();
	}

	public PropertyDescription(BeanDescription bean, String propertyName, String propertyClassName) {
		super();
		this.bean = bean;
		this.propertyName = propertyName;
		this.propertyClassName = propertyClassName;
	}

	private BeanDescription bean;
	private String propertyName;
	private String propertyClassName;
	private boolean mapped = false;
	private boolean displayed = false;
	private List<MapPropertyProfileDescription> mapPropertyProfiles = new ArrayList<MapPropertyProfileDescription>();
	private String captionText = null;
	private String captionProp = null;
	private String converterClassName = null;
	
	private List<DisplayPropertyProfileDescription> displayPropertyProfiles = new ArrayList<DisplayPropertyProfileDescription>(
			Collections.singletonList(new DisplayPropertyProfileDescription(
					this, Constants.DEFAULT_DISPLAY_PROFILE)));

	public BeanDescription getBean() {
		return bean;
	}

	public void setBean(BeanDescription bean) {
		this.bean = bean;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyGetterName() {
		if (getPropertyName() == null) {
			return null;
		}
		return (("boolean".equals(getPropertyClassName()) || "Boolean".equals(getPropertyClassName()) || "java.lang.Boolean"
				.equals(getPropertyClassName())) ? "is" : "get") + getPropertyName().substring(0, 1).toUpperCase() + getPropertyName().substring(1);
	}

	public String getPropertySetterName() {
		if (getPropertyName() == null) {
			return null;
		}
		return "set" + getPropertyName().substring(0, 1).toUpperCase() + getPropertyName().substring(1);
	}

	public String getPropertyObtainMethodName() {
		if (getPropertyName() == null) {
			return null;
		}
		return "obtainAuswahlFor" + getPropertyName().substring(0, 1).toUpperCase() + getPropertyName().substring(1);
	}

	public String getPropertySetChoicesForMethodName() {
		if (getPropertyName() == null) {
			return null;
		}
		return "setChoicesFor" + getPropertyName().substring(0, 1).toUpperCase() + getPropertyName().substring(1);
	}

	public String getPropertyChoicesForParamName() {
		if (getPropertyName() == null) {
			return null;
		}
		return "choicesFor" + getPropertyName().substring(0, 1).toUpperCase() + getPropertyName().substring(1);
	}

	public String getPropertyClassName() {
		return propertyClassName;
	}

	public void setPropertyClassName(String propertyClassName) {
		this.propertyClassName = propertyClassName;
	}

	public boolean isPropertyUnboxed() {
		return boxingReplacements.containsKey(getPropertyClassName());
	}

	public String getPropertyClassNameBoxed() {
		return boxingReplacements.containsKey(getPropertyClassName()) ? boxingReplacements.get(getPropertyClassName()) : getPropertyClassName();
	}

	public String getPropertyUnboxedFromString() {
		return boxingReplacements.containsKey(getPropertyClassName()) ? (boxingReplacements.get(getPropertyClassName()) + ".parse"
				+ getPropertyClassName().substring(0, 1).toUpperCase() + getPropertyClassName().substring(1)) : "";
	}

	public String getPropertyUnboxedToString() {
		return boxingReplacements.containsKey(getPropertyClassName()) ? (boxingReplacements.get(getPropertyClassName()) + ".toString") : "";
	}

	public boolean isPropertyClassRange() {
		return rangeDataTypes.contains(getPropertyClassName());
	}

	public boolean isMapped() {
		return mapped;
	}

	public void setMapped(boolean mapped) {
		this.mapped = mapped;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	public boolean isContainedInMapProfile(MapProfileDescription profileToCheck) {
		boolean hasIncludes = false;
		for (PropertyDescription p : bean.getProperties()) {
			if (p.getMapPropertyProfile(profileToCheck).isIncluded()) {
				hasIncludes = true;
				break;
			}
		}
		if (hasIncludes) {
			return getMapPropertyProfile(profileToCheck).isIncluded() && (!getMapPropertyProfile(profileToCheck).isExcluded());
		} else {
			// no includes = all includes
			return (!getMapPropertyProfile(profileToCheck).isExcluded());
		}
	}

	public List<MapPropertyProfileDescription> getMapPropertyProfiles() {
		return mapPropertyProfiles;
	}

	public void setMapPropertyProfiles(List<MapPropertyProfileDescription> mapPropertyProfiles) {
		this.mapPropertyProfiles = mapPropertyProfiles;
	}

	public void addMapPropertyProfile(MapPropertyProfileDescription mapPropertyProfile) {
		mapPropertyProfiles.add(mapPropertyProfile);
	}

	public void removePropertyProfile(MapPropertyProfileDescription mapPropertyProfile) {
		mapPropertyProfiles.remove(mapPropertyProfile);
	}

	public MapPropertyProfileDescription getMapPropertyProfile(MapProfileDescription profile) {
		return getMapPropertyProfile(profile.getProfileName());
	}

	public MapPropertyProfileDescription getMapPropertyProfile(String profileName) {
		for (MapPropertyProfileDescription p : getMapPropertyProfiles()) {
			if (p.getProfileName().equals(profileName)) {
				return p;
			}
		}
		addMapPropertyProfile(new MapPropertyProfileDescription(this, profileName, getPropertyName()));
		return getMapPropertyProfile(profileName);
	}

	public String getCaptionText() {
		return captionText;
	}

	public void setCaptionText(String captionText) {
		this.captionText = captionText;
	}

	public String getCaptionProp() {
		return captionProp;
	}

	public void setCaptionProp(String captionProp) {
		this.captionProp = captionProp;
	}

	public String getEffectiveCaption() {
		if (getCaptionText() != null) {
			return getCaptionText();
		}
		return getPropertyName().substring(0, 1).toUpperCase() + getPropertyName().substring(1);
	}

	public boolean isContainedInDisplayProfile(DisplayProfileDescription profileToCheck) {
		boolean hasIncludes = false;
		for (PropertyDescription p : bean.getProperties()) {
			if (p.getDisplayPropertyProfile(profileToCheck).isIncluded()) {
				hasIncludes = true;
				break;
			}
		}
		if (hasIncludes) {
			return getDisplayPropertyProfile(profileToCheck).isIncluded() && (!getDisplayPropertyProfile(profileToCheck).isExcluded());
		} else {
			// no includes = all includes
			return (!getDisplayPropertyProfile(profileToCheck).isExcluded());
		}
	}

	public List<DisplayPropertyProfileDescription> getDisplayPropertyProfiles() {
		return displayPropertyProfiles;
	}

	public void setDisplayPropertyProfiles(List<DisplayPropertyProfileDescription> displayPropertyProfiles) {
		this.displayPropertyProfiles = displayPropertyProfiles;
	}

	public void addDisplayPropertyProfile(DisplayPropertyProfileDescription displayPropertyProfile) {
		displayPropertyProfiles.add(displayPropertyProfile);
	}

	public void removeDisplayPropertyProfile(DisplayPropertyProfileDescription displayPropertyProfile) {
		displayPropertyProfiles.remove(displayPropertyProfile);
	}

	public DisplayPropertyProfileDescription getDisplayPropertyProfile(DisplayProfileDescription profile) {
		return getDisplayPropertyProfile(profile.getProfileName());
	}

	public DisplayPropertyProfileDescription getDisplayPropertyProfile(String profileName) {
		for (DisplayPropertyProfileDescription p : getDisplayPropertyProfiles()) {
			if (p.getProfileName().equals(profileName)) {
				return p;
			}
		}
		addDisplayPropertyProfile(new DisplayPropertyProfileDescription(this, profileName));
		return getDisplayPropertyProfile(profileName);
	}

	public BeanDescription getEnumClass(List<BeanDescription> candidates) {
		for (BeanDescription candidate : candidates) {
			if (candidate.getClassName() != null && candidate.getClassName().equals(getPropertyClassName()) && candidate.isEnumeration()) {
				return candidate;
			}
		}
		return null;
	}

	public boolean hasEnumClass(List<BeanDescription> candidates) {
		return getEnumClass(candidates) != null;
	}

	public String getConverterClassName() {
		return converterClassName;
	}

	public void setConverterClassName(String converterClassName) {
		this.converterClassName = converterClassName;
	}

	@Override
	public String toString() {
		return "PropertyDescription [propertyName=" + propertyName + ", propertyClassName=" + propertyClassName + ", mapped=" + mapped
				+ ", displayed=" + displayed + ", captionText=" + captionText + ", captionProp=" + captionProp + ", getPropertyGetterName()="
				+ getPropertyGetterName() + ", getPropertySetterName()=" + getPropertySetterName() + ", getPropertyObtainMethodName()="
				+ getPropertyObtainMethodName() + ", getPropertyClassNameBoxed()=" + getPropertyClassNameBoxed() + ", isPropertyClassRange()"
				+ isPropertyClassRange() + ", getEffectiveCaption()=" + getEffectiveCaption() + "]";
	}

	private static final Map<String, String> boxingReplacements = new HashMap<String, String>();
	static {
		boxingReplacements.put("boolean", "Boolean");
		boxingReplacements.put("byte", "Byte");
		boxingReplacements.put("char", "Character");
		boxingReplacements.put("double", "Double");
		boxingReplacements.put("float", "Float");
		boxingReplacements.put("int", "Integer");
		boxingReplacements.put("long", "Long");
		boxingReplacements.put("short", "Short");
	}

	private static final Set<String> rangeDataTypes = new HashSet<String>();
	static {
		rangeDataTypes.add("short");
		rangeDataTypes.add("Short");
		rangeDataTypes.add("java.lang.Short");
		rangeDataTypes.add("int");
		rangeDataTypes.add("Integer");
		rangeDataTypes.add("java.lang.Integer");
		rangeDataTypes.add("long");
		rangeDataTypes.add("Long");
		rangeDataTypes.add("java.lang.Long");
		rangeDataTypes.add("float");
		rangeDataTypes.add("Float");
		rangeDataTypes.add("java.lang.Float");
		rangeDataTypes.add("double");
		rangeDataTypes.add("Double");
		rangeDataTypes.add("java.lang.Double");
		rangeDataTypes.add("BigInteger");
		rangeDataTypes.add("java.math.BigInteger");
		rangeDataTypes.add("BigDecimal");
		rangeDataTypes.add("java.math.BigDecimal");
		rangeDataTypes.add("Date");
		rangeDataTypes.add("java.util.Date");
	}

}
