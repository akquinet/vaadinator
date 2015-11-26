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

public class ConstructorDescription {

	public ConstructorDescription() {
		super();
	}

	public ConstructorDescription(BeanDescription bean) {
		super();
		this.bean = bean;
	}

	private BeanDescription bean;
	private List<ConstructorParamDescription> params = new ArrayList<ConstructorParamDescription>();

	public BeanDescription getBean() {
		return bean;
	}

	public void setBean(BeanDescription bean) {
		this.bean = bean;
	}

	public List<ConstructorParamDescription> getParams() {
		return params;
	}

	public void setParams(List<ConstructorParamDescription> params) {
		this.params = params;
	}

	public void addParam(ConstructorParamDescription param) {
		params.add(param);
	}

	public void removeParam(ConstructorParamDescription param) {
		params.remove(param);
	}

	public String getConstructorParamStr() {
		String res = "";
		for (ConstructorParamDescription param : getParams()) {
			if (res.length() > 0) {
				res += ", ";
			}
			res += (param.getParamClassName() + " " + param.getParamName());
		}
		return res;
	}

	public String getConstructorParamPassStr() {
		String res = "";
		for (ConstructorParamDescription param : getParams()) {
			if (res.length() > 0) {
				res += ", ";
			}
			res += param.getParamName();
		}
		return res;
	}

	@Override
	public String toString() {
		return "ConstructorDescription [getConstructorParamStr()=" + getConstructorParamStr() + ", getConstructorParamPassStr()="
				+ getConstructorParamPassStr() + "]";
	}

}
