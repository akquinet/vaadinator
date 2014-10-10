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
package de.akquinet.engineering.vaadinator.example.crmws.model;

import java.util.Iterator;

import de.akquinet.engineering.vaadinator.example.crmws.dto.ContactDto;
import de.akquinet.engineering.vaadinator.example.crmws.dto.HistoryDto;

public class ContactMapperEx extends ContactMapper {

	public ContactMapperEx() {
		super();
	}

	@Override
	public void mapContactInclHistory(Contact source, ContactDto target) {
		super.mapContactInclHistory(source, target);
		// TODO: be able to map Lists via generated Code
		Iterator<HistoryDto> targetHistoryIterator = target.getHistory().iterator();
		boolean didAdd = false;
		for (History history : source.getHistory()) {
			HistoryDto targetHistoryDto;
			if ((!didAdd) && targetHistoryIterator.hasNext()) {
				targetHistoryDto = targetHistoryIterator.next();
			} else {
				targetHistoryDto = createHistoryDto();
				target.getHistory().add(targetHistoryDto);
				didAdd = true;
			}
			createHistoryMapper().mapContactInclHistory(history, targetHistoryDto);
		}
	}

	protected HistoryDto createHistoryDto() {
		return new HistoryDto();
	}

	protected HistoryMapper createHistoryMapper() {
		return new HistoryMapper();
	}

}
