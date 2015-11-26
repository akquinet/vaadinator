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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisplayProfileDescription {

	public DisplayProfileDescription() {
		super();
	}

	public DisplayProfileDescription(BeanDescription bean, String profileName) {
		super();
		this.bean = bean;
		this.profileName = profileName;
	}

	private BeanDescription bean;
	private String profileName;
	private String profileCaptionProp = null;
	private String profileCaptionText = null;
	private int order = 0;
	private boolean showOnFirstPage = true;

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

	public String getProfileClassName() {
		if (getProfileName() == null) {
			return null;
		}
		return getProfileName().substring(0, 1).toUpperCase() + getProfileName().substring(1);
	}

	public String getProfileCaptionProp() {
		return profileCaptionProp;
	}

	public void setProfileCaptionProp(String profileCaptionProp) {
		this.profileCaptionProp = profileCaptionProp;
	}

	public String getProfileCaptionText() {
		return profileCaptionText;
	}

	public void setProfileCaptionText(String profileCaptionText) {
		this.profileCaptionText = profileCaptionText;
	}

	public String getEffectiveProfileCaption() {
		if (getProfileCaptionText() != null) {
			return getProfileCaptionText();
		}
		return getProfileName();
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isShowOnFirstPage() {
		return showOnFirstPage;
	}

	public void setShowOnFirstPage(boolean showOnFirstPage) {
		this.showOnFirstPage = showOnFirstPage;
	}

	public List<PropertyDescription> getPropertiesInProfile() {
		if (getBean() == null) {
			return null;
		}
		if (getBean().getDisplayProperties() == null) {
			return null;
		}
		List<PropertyDescription> res = new ArrayList<PropertyDescription>();
		// includes first
		for (PropertyDescription p : getBean().getDisplayProperties()) {
			if (p.isDisplayed() && p.getDisplayPropertyProfile(this).isIncluded() && (!p.getDisplayPropertyProfile(this).isExcluded())) {
				res.add(p);
			}
		}
		// no includes = all includes
		if (res.size() == 0) {
			for (PropertyDescription p : getBean().getDisplayProperties()) {
				if (p.isDisplayed() && (!p.getDisplayPropertyProfile(this).isExcluded())) {
					res.add(p);
				}
			}
		}
		return res;
	}

	public List<SectionDescription> getSectionsInProfile() {
		List<SectionDescription> res = new ArrayList<SectionDescription>();
		for (PropertyDescription p : getPropertiesInProfile()) {
			SectionDescription desc = new SectionDescription(p
					.getDisplayPropertyProfile(this).getSectionName());
			if (!res.contains(desc)) {
				res.add(desc);
			}
		}
		return res;
	}

	public List<PropertyDescription> getSortedPropertiesInProfileForDetail() {
		List<PropertyDescription> res = new ArrayList<PropertyDescription>();
		for (PropertyDescription p : getPropertiesInProfile()) {
			if (p.getDisplayPropertyProfile(DisplayProfileDescription.this).isShowInDetail()) {
				res.add(p);
			}
		}
		if (res.size() == 0) {
			res.addAll(getPropertiesInProfile());
		}
		Collections.sort(res, new Comparator<PropertyDescription>() {

			@Override
			public int compare(PropertyDescription o1, PropertyDescription o2) {
				return Integer.compare(o1.getDisplayPropertyProfile(DisplayProfileDescription.this).getOrder(),
						o2.getDisplayPropertyProfile(DisplayProfileDescription.this).getOrder());
			}
		});
		return res;
	}

	public String getSortedPropertiesInProfileForDetailStr() {
		String res = "";
		for (PropertyDescription p : getSortedPropertiesInProfileForDetail()) {
			if (res.length() > 0) {
				res += ", ";
			}
			res += ("\"" + p.getPropertyName() + "\"");
		}
		return res;
	}

	public List<PropertyDescription> getSortedPropertiesInProfileForTable() {
		List<PropertyDescription> res = new ArrayList<PropertyDescription>();
		for (PropertyDescription p : getPropertiesInProfile()) {
			if (p.getDisplayPropertyProfile(DisplayProfileDescription.this).isShowInTable()) {
				res.add(p);
			}
		}
		if (res.size() == 0) {
			res.addAll(getPropertiesInProfile());
		}
		Collections.sort(res, new Comparator<PropertyDescription>() {

			@Override
			public int compare(PropertyDescription o1, PropertyDescription o2) {
				return Integer.compare(o1.getDisplayPropertyProfile(DisplayProfileDescription.this).getOrder(),
						o2.getDisplayPropertyProfile(DisplayProfileDescription.this).getOrder());
			}
		});
		return res;
	}

	public String getSortedPropertiesInProfileForTableStr() {
		String res = "";
		for (PropertyDescription p : getSortedPropertiesInProfileForTable()) {
			if (res.length() > 0) {
				res += ", ";
			}
			res += ("\"" + p.getPropertyName() + "\"");
		}
		return res;
	}

	@Override
	public String toString() {
		return "DisplayProfileDescription [profileName=" + profileName + ", profileCaptionProp=" + profileCaptionProp + ", profileCaptionText="
				+ profileCaptionText + ", order=" + order + ", showOnFirstPage=" + showOnFirstPage + ", getEffectiveProfileCaption()="
				+ getEffectiveProfileCaption() + ", getProfileClassName()=" + getProfileClassName() + ", getSortedPropertiesInProfileForDetailStr()="
				+ getSortedPropertiesInProfileForDetailStr() + ", getSortedPropertiesInProfileForTableStr()="
				+ getSortedPropertiesInProfileForTableStr() + "]";
	}
}
