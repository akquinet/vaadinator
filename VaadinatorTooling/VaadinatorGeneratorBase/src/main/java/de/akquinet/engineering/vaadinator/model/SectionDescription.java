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

public class SectionDescription {

	public SectionDescription() {
		super();
	}

	public SectionDescription(String sectionName) {
		super();
		this.sectionName = sectionName;
	}

	private String sectionName;

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionVarSuffix() {
		if (getSectionName() == null) {
			return null;
		}
		String res = "";
		for (String part : getSectionName().split(" ")) {
			part = part.trim();
			res += (part.substring(0, 1).toUpperCase() + part.substring(1));
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sectionName == null) ? 0 : sectionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SectionDescription other = (SectionDescription) obj;
		if (sectionName == null) {
			if (other.sectionName != null)
				return false;
		} else if (!sectionName.equals(other.sectionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SectionDescription [sectionName=" + sectionName + ", getSectionVarSuffix()=" + getSectionVarSuffix() + "]";
	}

}
