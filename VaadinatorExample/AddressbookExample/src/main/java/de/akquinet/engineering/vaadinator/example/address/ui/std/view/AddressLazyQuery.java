package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.lazyquerycontainer.Query;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.model.AddressQuery;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressListView.Observer;

public class AddressLazyQuery implements Query {

	private AddressQuery addressQuery;
	private Observer observer;

	public AddressLazyQuery(Observer observer, AddressQuery addressQuery) {
		this.addressQuery = addressQuery;
		this.observer = observer;
	}

	@Override
	public int size() {
		return (int) observer.onCountTable(addressQuery);
	}

	@Override
	public List<Item> loadItems(int startIndex, int count) {
		addressQuery.setFirstResult(startIndex);
		addressQuery.setMaxResults(count);

		List<Address> beans = observer.onRefreshTable(addressQuery);
		List<Item> items = new ArrayList<Item>();
		for (Address bean : beans) {
			items.add(new BeanItem<Address>(bean));
		}
		return items;
	}

	@Override
	public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
		throw new UnsupportedOperationException("This container is readonly");

	}

	@Override
	public boolean deleteAllItems() {
		throw new UnsupportedOperationException("This container is readonly");
	}

	@Override
	public Item constructItem() {
		throw new UnsupportedOperationException("This container is readonly");
	}

}
