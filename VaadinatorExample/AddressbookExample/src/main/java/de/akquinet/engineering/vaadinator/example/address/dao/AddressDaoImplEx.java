package de.akquinet.engineering.vaadinator.example.address.dao;

import javax.persistence.EntityManager;

import de.akquinet.engineering.vaadinator.example.address.model.AddressProperties;

public class AddressDaoImplEx extends AddressDaoImpl {

	public AddressDaoImplEx() {
		super();
	}

	public AddressDaoImplEx(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected String toOrderBy(AddressProperties property, String order) {
		if (property == AddressProperties.NAME) {
			return "address.vorname " + order + ", address.nachname " + order;
		}
		return super.toOrderBy(property, order);

	}
}
