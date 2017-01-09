package de.akquinet.engineering.vaadinator.example.address.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.DisplayPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.FieldType;

@DisplayBean
@Entity
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@DisplayProperty(profileSettings = { @DisplayPropertySetting(showInTable = true) })
	private String name;
	@DisplayProperty(profileSettings = {
			@DisplayPropertySetting(fieldType = FieldType.CUSTOM, customClassName = "AddressSelectField") })
	private Address leader;
	@DisplayProperty(profileSettings = { @DisplayPropertySetting(showInTable = true, showInDetail = false) })
	public String getLeaderName() {
		return leader != null ? leader.getName() : "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getLeader() {
		return leader;
	}

	public void setLeader(Address leader) {
		this.leader = leader;
	}
}
