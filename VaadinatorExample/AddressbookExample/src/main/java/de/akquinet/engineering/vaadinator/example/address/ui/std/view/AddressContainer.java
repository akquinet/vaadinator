package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import java.sql.Date;

import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class AddressContainer extends LazyQueryContainer {

	public AddressContainer(QueryDefinition queryDefinition, AddressLazyQueryFactory queryFactory) {
		super(queryDefinition, queryFactory);

		addContainerProperty("name", String.class, "", true, true);
		addContainerProperty("geburtsdatum", Date.class, "", true, true);
		addContainerProperty("email", String.class, "", true, true);

	}

	private static final long serialVersionUID = 1L;

}
