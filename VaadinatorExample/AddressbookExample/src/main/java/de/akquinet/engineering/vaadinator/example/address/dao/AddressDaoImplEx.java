package de.akquinet.engineering.vaadinator.example.address.dao;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.AddressProperties;
import de.akquinet.engineering.vaadinator.example.address.model.AddressQuery;

public class AddressDaoImplEx extends AddressDaoImpl {

	private static final Logger LOGGER = Logger.getLogger(AddressDaoImplEx.class.getName());

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

	@Override
	public List<Address> list(AddressQuery addressQuery, Map<String, Object> context) {
		LOGGER.info("AddressDao.list(...) with query: " + addressQuery);
		return super.list(addressQuery, context);
	}
}
