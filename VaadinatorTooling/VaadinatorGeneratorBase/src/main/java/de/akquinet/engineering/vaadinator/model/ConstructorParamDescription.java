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

public class ConstructorParamDescription {

	public ConstructorParamDescription() {
		super();
	}

	public ConstructorParamDescription(ConstructorDescription constructor, String paramName, String paramClassName) {
		super();
		this.constructor = constructor;
		this.paramName = paramName;
		this.paramClassName = paramClassName;
	}

	private ConstructorDescription constructor;
	private String paramName;
	private String paramClassName;

	public ConstructorDescription getConstructor() {
		return constructor;
	}

	public void setConstructor(ConstructorDescription constructor) {
		this.constructor = constructor;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamClassName() {
		return paramClassName;
	}

	public void setParamClassName(String paramClassName) {
		this.paramClassName = paramClassName;
	}

	@Override
	public String toString() {
		return "ConstructorParamDescription [paramName=" + paramName + ", paramClassName=" + paramClassName + "]";
	}

}
