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
package de.akquinet.engineering.vaadinator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DisplayProperty {
	String captionText() default "";

	String captionProp() default "";

	DisplayPropertySetting[] profileSettings() default {};

	boolean ignore() default false;
	
	/**
	 * The name of the converter class which should be used for the input field.
	 * <p>
	 * If the name is not a full qualified class name the package com.vaadin.data.util.converter will be used
	 */
	String converterClassName() default "";
		
	/**
	 * Indicates weather a property is sortable
	 */
	boolean sortable() default true;

}
