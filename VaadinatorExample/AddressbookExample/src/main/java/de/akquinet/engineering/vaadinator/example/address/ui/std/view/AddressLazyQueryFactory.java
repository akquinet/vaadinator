package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import java.util.List;

import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.vaadin.data.Container.Filter;

import de.akquinet.engineering.vaadinator.example.address.model.AddressProperties;
import de.akquinet.engineering.vaadinator.example.address.model.AddressQuery;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressListView.Observer;

public class AddressLazyQueryFactory implements QueryFactory {
	private final AddressListView.Observer observer;

	public AddressLazyQueryFactory(Observer observer) {
		super();
		this.observer = observer;
	}

	protected AddressQuery translate(QueryDefinition queryDefinition) {
		AddressQuery addressQuery = new AddressQuery();
		handleSorting(queryDefinition, addressQuery);
		handleFiltering(queryDefinition.getFilters(), addressQuery);
		handleFiltering(queryDefinition.getDefaultFilters(), addressQuery);
		return addressQuery;
	}

	protected void handleFiltering(List<Filter> filters, AddressQuery addressQuery) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO handle more than one sort property, which is possible e.g. in grid
	 * 
	 * @param queryDefinition
	 * @param addressQuery
	 */
	protected void handleSorting(QueryDefinition queryDefinition, AddressQuery addressQuery) {
		if (queryDefinition.getSortPropertyIds() != null && queryDefinition.getSortPropertyIds().length > 0) {
			String sortPropery = String.valueOf(queryDefinition.getSortPropertyIds()[0]);
			AddressProperties orderBy = AddressProperties.valueOf(sortPropery.toUpperCase());
			addressQuery.setOrderBy(orderBy);
			addressQuery.setOrderDescending(!queryDefinition.getSortPropertyAscendingStates()[0]);
		}
	}

	@Override
	public Query constructQuery(QueryDefinition queryDefinition) {
		return new AddressLazyQuery(observer, translate(queryDefinition));
	}

}
