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

public class EnumValueDescription {

	public EnumValueDescription() {
		super();
	}

	public EnumValueDescription(BeanDescription bean, String value) {
		super();
		this.bean = bean;
		this.value = value;
	}

	private BeanDescription bean;
	private String value;
	private String captionText = null;
	private String captionProp = null;

	public BeanDescription getBean() {
		return bean;
	}

	public void setBean(BeanDescription bean) {
		this.bean = bean;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
		return getValue().substring(0, 1).toUpperCase() + getValue().substring(1).toLowerCase();
	}

	@Override
	public String toString() {
		return "EnumValueDescription [bean=" + bean + ", value=" + value + ", captionText=" + captionText + ", captionProp=" + captionProp
				+ ", getEffectiveCaption()=" + getEffectiveCaption() + "]";
	}

}
