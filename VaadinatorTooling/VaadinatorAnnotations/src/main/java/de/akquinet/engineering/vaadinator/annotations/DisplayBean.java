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
@Target({ ElementType.TYPE })
public @interface DisplayBean {
	String captionText() default "";

	String captionProp() default "";

	DisplayBeanSetting[] profiles() default { @DisplayBeanSetting(profileName = Constants.DEFAULT_DISPLAY_PROFILE) };

	boolean ignore() default false;
	
	/**
	 * If set to true, java bean validation (JSR-303) will be enabled.
	 * <p>
	 * A JSR-303 implementation must be present at the classpath.
	 */
	boolean beanValidation() default false;
}
