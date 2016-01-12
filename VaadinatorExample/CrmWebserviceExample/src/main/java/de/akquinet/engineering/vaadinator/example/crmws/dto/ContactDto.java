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
package de.akquinet.engineering.vaadinator.example.crmws.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.engineering.vaadinator.example.crmws.model.History;

public class ContactDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long revision;
	private String name;
	private List<HistoryDto> history = new ArrayList<HistoryDto>();
	
	private HistoryDto historyOne;

	public HistoryDto getHistoryOne() {
		return historyOne;
	}

	public void setHistoryOne(HistoryDto historyOne) {
		this.historyOne = historyOne;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HistoryDto> getHistory() {
		return history;
	}

	public void setHistory(List<HistoryDto> history) {
		this.history = history;
	}

}
