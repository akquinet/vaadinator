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
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.akquinet.engineering.vaadinator.annotations.MapBean;
import de.akquinet.engineering.vaadinator.annotations.MapBeanSetting;
import de.akquinet.engineering.vaadinator.annotations.MapProperty;
import de.akquinet.engineering.vaadinator.annotations.MapPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.ServiceBean;
import de.akquinet.engineering.vaadinator.example.crmws.dto.HistoryDto;

@MapBean(profiles = { @MapBeanSetting(profileName = "contactInclHistory", target = HistoryDto.class),
		@MapBeanSetting(profileName = "historyOnly", target = HistoryDto.class, bidirectional = true) })
@Entity
@Table(name="CrmHistory")
@ServiceBean
public class History implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@MapProperty(profileSettings = { @MapPropertySetting(profileName = "contactInclHistory", readonly = true),
			@MapPropertySetting(profileName = "historyOnly", readonly = true) })
	private long id;
	@MapProperty
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp = new Date();
	@MapProperty
	private String text;
	@ManyToOne(fetch = FetchType.LAZY)
	private Contact parentContact = null;

	public long getId() {
		return id;
	}
	
	void setId(long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Contact getParentContact() {
		return parentContact;
	}

	public void setParentContact(Contact parentContact) {
		this.parentContact = parentContact;
	}

}
