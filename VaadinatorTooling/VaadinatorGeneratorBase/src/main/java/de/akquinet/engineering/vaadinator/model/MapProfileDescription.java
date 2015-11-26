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
import java.util.List;

public class MapProfileDescription {

	public MapProfileDescription() {
		super();
	}

	public MapProfileDescription(BeanDescription bean, String profileName, String targetClassName) {
		super();
		this.bean = bean;
		this.profileName = profileName;
		this.targetClassName = targetClassName;
	}

	private BeanDescription bean;
	private String profileName;
	private String targetClassName;
	private boolean bidirectional = false;

	public BeanDescription getBean() {
		return bean;
	}

	public void setBean(BeanDescription bean) {
		this.bean = bean;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileMapperName() {
		if (getProfileName() == null) {
			return null;
		}
		return "map" + getProfileName().substring(0, 1).toUpperCase() + getProfileName().substring(1);
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public boolean isBidirectional() {
		return bidirectional;
	}

	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}

	public List<PropertyDescription> getPropertiesInProfile() {
		if (getBean() == null) {
			return null;
		}
		if (getBean().getMapProperties() == null) {
			return null;
		}
		List<PropertyDescription> res = new ArrayList<PropertyDescription>();
		// includes first
		for (PropertyDescription p : getBean().getMapProperties()) {
			if (p.isMapped() && p.getMapPropertyProfile(this).isIncluded() && (!p.getMapPropertyProfile(this).isExcluded())) {
				res.add(p);
			}
		}
		// no includes = all includes
		if (res.size() == 0) {
			for (PropertyDescription p : getBean().getMapProperties()) {
				if (p.isMapped() && (!p.getMapPropertyProfile(this).isExcluded())) {
					res.add(p);
				}
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return "MapProfileDescription [profileName=" + profileName + ", targetClassName=" + targetClassName + ", bidirectional=" + bidirectional
				+ ", getProfileMapperName()=" + getProfileMapperName() + "]";
	}

}
