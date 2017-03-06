/*
 * Copyright 2016 akquinet engineering GmbH
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
package de.akquinet.engineering.vaadinator.example.address.model;

import de.akquinet.engineering.vaadinator.annotations.DisplayEnum;

public enum Filme {

	@DisplayEnum(captionText="I heart Huckabees")
	I_HEART_HUCKABEES, 
	@DisplayEnum(captionText="Lost in Translation")
	LOST_IN_TRANSLATION,
	@DisplayEnum(captionText="Anleitung zum Unglücklichsein")
	ANLEITUNG_UNGLUECKLICHSEIN
	
}
