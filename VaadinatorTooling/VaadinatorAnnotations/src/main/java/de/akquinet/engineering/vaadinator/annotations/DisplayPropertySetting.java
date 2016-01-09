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
import java.util.Collection;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface DisplayPropertySetting {
	String profileName() default Constants.DEFAULT_DISPLAY_PROFILE;

	boolean include() default false;

	boolean exclude() default false;

	FieldType fieldType() default FieldType.TEXTFIELD;

	String customClassName() default "";

	boolean customAuswahlAusListe() default false;

	/**
	 * Indicates if the custom class is a multi select vaadin component.
	 * <p>
	 * The property must be a typed {@link Collection}. The vaadin component
	 * must implement the methods {@code addItem(Object)} and
	 * {@code addItemCaption(Object, String)} like a
	 * {@link com.vaadin.ui.AbstractSelect}.
	 * 
	 **/
	boolean customMultiAuswahlAusListe() default false;

	boolean customUnboxed() default false;

	String sectionName() default "Basisdaten";

	int order() default 0;

	boolean showInTable() default false;

	boolean showInDetail() default true;

	float tableExpandRatio() default 1.0f;

	boolean readOnly() default false;

	boolean required() default false;
}
