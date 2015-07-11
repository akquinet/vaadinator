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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.akquinet.engineering.vaadinator.annotations.Constants;

public class BeanDescription {

	public BeanDescription() {
		super();
	}

	public BeanDescription(String className) {
		super();
		this.className = className;
	}

	public BeanDescription(String className, boolean enumeration) {
		super();
		this.className = className;
		this.enumeration = enumeration;
	}

	private String className;
	private String superClassName;
	private boolean mapped = false;
	private boolean displayed = false;
	private boolean service = false;
	private boolean wrapped = false;
	private boolean enumeration = false;
	private List<MapProfileDescription> mapProfiles = new ArrayList<MapProfileDescription>();
	private List<DisplayProfileDescription> displayProfiles = new ArrayList<DisplayProfileDescription>(
			Collections.singletonList(new DisplayProfileDescription(this,
					Constants.DEFAULT_DISPLAY_PROFILE)));
	private List<PropertyDescription> properties = new ArrayList<PropertyDescription>();
	private List<EnumValueDescription> enumValues = new ArrayList<EnumValueDescription>();
	private List<String> imports = new ArrayList<String>();
	private String captionText = null;
	private String captionProp = null;
	private List<ConstructorDescription> constructors = new ArrayList<ConstructorDescription>();
	private List<String> getters = new ArrayList<String>();
	private List<String> setters = new ArrayList<String>();
	private String pckg;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public boolean hasSuperClass() {
		return getSuperClassName() != null && getSuperClassName().length() > 0;
	}

	public String getClassNameMultiple() {
		if (getClassName() == null) {
			return null;
		}
		if (getClassName().endsWith("s")) {
			return getClassName() + "es";
		}
		return getClassName() + "s";
	}

	public String getClassNamePass() {
		return getClassName().substring(0, 1).toLowerCase() + getClassName().substring(1);
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
	
	public boolean isService() {
		return service;
	}
	
	public void setService(boolean service) {
		this.service = service;
	}

	public boolean isWrapped() {
		return wrapped;
	}

	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
	}

	public boolean isEnumeration() {
		return enumeration;
	}

	public void setEnumeration(boolean enumeration) {
		this.enumeration = enumeration;
	}

	public List<MapProfileDescription> getMapProfiles() {
		return mapProfiles;
	}

	public void setProfiles(List<MapProfileDescription> mapProfiles) {
		this.mapProfiles = mapProfiles;
	}

	public void addMapProfile(MapProfileDescription mapProfile) {
		mapProfiles.add(mapProfile);
	}

	public void removeMapProfile(MapProfileDescription mapProfile) {
		mapProfiles.remove(mapProfile);
	}

	public MapProfileDescription getMapProfile(String profileName) {
		for (MapProfileDescription p : getMapProfiles()) {
			if (p.getProfileName().equals(profileName)) {
				return p;
			}
		}
		addMapProfile(new MapProfileDescription(this, profileName, null));
		return getMapProfile(profileName);
	}

	public List<DisplayProfileDescription> getDisplayProfiles() {
		return displayProfiles;
	}

	public void setDisplayProfiles(List<DisplayProfileDescription> displayProfiles) {
		this.displayProfiles = displayProfiles;
	}

	public void addDisplayProfile(DisplayProfileDescription displayProfile) {
		displayProfiles.add(displayProfile);
	}

	public void removeDisplayProfile(DisplayProfileDescription displayProfile) {
		displayProfiles.remove(displayProfile);
	}

	public DisplayProfileDescription getDisplayProfile(String profileName) {
		for (DisplayProfileDescription p : getDisplayProfiles()) {
			if (p.getProfileName().equals(profileName)) {
				return p;
			}
		}
		addDisplayProfile(new DisplayProfileDescription(this, profileName));
		return getDisplayProfile(profileName);
	}

	public List<PropertyDescription> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyDescription> properties) {
		this.properties = properties;
	}

	public void addProperty(PropertyDescription property) {
		properties.add(property);
	}

	public void removeProperty(PropertyDescription property) {
		properties.remove(property);
	}

	public PropertyDescription getProperty(String propertyName) {
		for (PropertyDescription p : getProperties()) {
			if (p.getPropertyName().equals(propertyName)) {
				return p;
			}
		}
		// return null if non-existent for purpose
		return null;
	}

	public List<PropertyDescription> getMapProperties() {
		List<PropertyDescription> res = new ArrayList<PropertyDescription>();
		for (PropertyDescription p : getProperties()) {
			if (p.isMapped()) {
				res.add(p);
			}
		}
		return res;
	}

	public List<PropertyDescription> getDisplayProperties() {
		List<PropertyDescription> res = new ArrayList<PropertyDescription>();
		for (PropertyDescription p : getProperties()) {
			if (p.isDisplayed()) {
				res.add(p);
			}
		}
		return res;
	}

	public Set<String> getDeepPropertyClassNames() {
		Set<String> res = new HashSet<String>();
		for (PropertyDescription p : getProperties()) {
			if (p.isMapped()) {
				for (MapPropertyProfileDescription pp : p.getMapPropertyProfiles()) {
					if (pp.isDeep()) {
						res.add(p.getPropertyClassName());
					}
				}
			}
		}
		return res;
	}

	public List<String> getSectionNames() {
		List<String> res = new ArrayList<String>();
		for (PropertyDescription p : getProperties()) {
			if (p.isDisplayed()) {
				for (DisplayPropertyProfileDescription pp : p.getDisplayPropertyProfiles()) {
					if (!res.contains(pp.getSectionName())) {
						res.add(pp.getSectionName());
					}
				}
			}
		}
		return res;
	}

	public List<EnumValueDescription> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(List<EnumValueDescription> enumValues) {
		this.enumValues = enumValues;
	}

	public void addEnumValue(EnumValueDescription enumValue) {
		enumValues.add(enumValue);
	}

	public void removeEnumValue(EnumValueDescription enumValue) {
		enumValues.remove(enumValue);
	}

	public EnumValueDescription getEnumValue(String value) {
		for (EnumValueDescription en : getEnumValues()) {
			if (en.getValue().equals(value)) {
				return en;
			}
		}
		// return null if non-existent for purpose
		return null;
	}

	public List<String> getImports() {
		return imports;
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	public void addImport(String imp) {
		imports.add(imp);
	}

	public void removeImport(String imp) {
		imports.remove(imp);
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
		return getClassName();
	}

	public String getEffectiveCaptionPlural() {
		return getEffectiveCaption() == null ? null : getEffectiveCaption() + "n";
	}

	public List<ConstructorDescription> getConstructors() {
		return constructors;
	}

	public void setConstructors(List<ConstructorDescription> constructors) {
		this.constructors = constructors;
	}

	public void addConstructor(ConstructorDescription constructor) {
		constructors.add(constructor);
	}

	public void removeConstructor(ConstructorDescription constructor) {
		constructors.remove(constructor);
	}

	public List<String> getGetters() {
		return getters;
	}

	public void setGetters(List<String> getters) {
		this.getters = getters;
	}

	public void addGetter(String getter) {
		getters.add(getter);
	}

	public void removeGetter(String getter) {
		getters.remove(getter);
	}

	public boolean hasGetter(String getter) {
		return getters.contains(getter);
	}

	public List<String> getSetters() {
		return setters;
	}

	public void setSetters(List<String> setters) {
		this.setters = setters;
	}

	public void addSetter(String setter) {
		setters.add(setter);
	}

	public void removeSetter(String setter) {
		setters.remove(setter);
	}

	public boolean hasSetter(String setter) {
		return setters.contains(setter);
	}

	public String getPckg() {
		return pckg;
	}

	public void setPckg(String pckg) {
		this.pckg = pckg;
	}

	public String getBasePckg() {
		return getPckg().substring(0, getPckg().lastIndexOf('.'));
	}

	public String getViewPckg(DisplayProfileDescription profile) {
		return getViewPckg(profile.getProfileName());
	}

	public String getViewPckg(String profileName) {
		return getBasePckg() + ".ui." + profileName + ".view";
	}

	public String getPresenterPckg(DisplayProfileDescription profile) {
		return getPresenterPckg(profile.getProfileName());
	}

	public String getPresenterPckg(String profileName) {
		return getBasePckg() + ".ui." + profileName + ".presenter";
	}

	@Override
	public String toString() {
		return "BeanDescription [className=" + className + ", superClassName=" + superClassName + ", mapped=" + mapped + ", displayed=" + displayed
				+ ", service=" + service+ ", wrapped=" + wrapped + ", enumeration=" + enumeration + ", captionText=" + captionText + ", captionProp=" + captionProp
				+ ", pckg=" + pckg + ", hasSuperClass()=" + hasSuperClass() + ", getClassNameMultiple()=" + getClassNameMultiple()
				+ ", getClassNamePass()=" + getClassNamePass() + ", getDeepPropertyClassNames()=" + getDeepPropertyClassNames()
				+ ", getEffectiveCaption()=" + getEffectiveCaption() + ", getEffectiveCaptionPlural()=" + getEffectiveCaptionPlural() + "]";
	}

}
