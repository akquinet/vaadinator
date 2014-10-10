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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.akquinet.engineering.vaadinator.annotations.MapBean;
import de.akquinet.engineering.vaadinator.annotations.MapBeanSetting;
import de.akquinet.engineering.vaadinator.annotations.MapProperty;
import de.akquinet.engineering.vaadinator.annotations.MapPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.ServiceBean;
import de.akquinet.engineering.vaadinator.example.crmws.dto.ContactDto;

@MapBean(profiles = { @MapBeanSetting(profileName = "contactWithoutHistory", target = ContactDto.class, bidirectional = true),
		@MapBeanSetting(profileName = "contactInclHistory", target = ContactDto.class) })
@Entity
@Table(name="CrmContact")
@ServiceBean
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@MapProperty(profileSettings = { @MapPropertySetting(profileName = "contactWithoutHistory", readonly = true),
			@MapPropertySetting(profileName = "contactInclHistory", readonly = true) })
	private long id;
	@MapProperty(profileSettings = { @MapPropertySetting(profileName = "contactWithoutHistory", targetPropertyName = "revision"),
			@MapPropertySetting(profileName = "contactInclHistory", targetPropertyName = "revision") })
	private long rev = 0;
	@MapProperty
	private String name;
	// (deliberately don't map this one!)
	private int score = 0;
	// excluded in both as we can't map Lists just yet
	@MapProperty(profileSettings = { @MapPropertySetting(profileName = "contactWithoutHistory", exclude = true),
			@MapPropertySetting(profileName = "contactInclHistory", /*deep = true,*/ exclude = true) })
	// composition
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<History> history = new ArrayList<History>();

	public long getId() {
		return id;
	}
	
	void setId(long id) {
		this.id = id;
	}

	public long getRev() {
		return rev;
	}

	public void setRev(long rev) {
		this.rev = rev;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<History> getHistory() {
		return history;
	}

	public void setHistory(List<History> history) {
		this.history = history;
	}

	public void addHistory(History history) {
		this.history.add(history);
	}

	public void removeHistory(History history) {
		this.history.remove(history);
	}

}
