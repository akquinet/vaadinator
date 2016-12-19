package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import java.util.List;

import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.container.AddressLazyQueryContainer;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.container.AddressLazyQueryFactory;
import de.akquinet.engineering.vaadinator.example.address.ui.view.ExceptionMappingStrategy;

public class AddressListViewImplEx extends AddressListViewImpl {

	private AddressListView.Observer observer;
	private AddressLazyQueryContainer container;

	public AddressListViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy) {
		super(exceptionMappingStrategy);
	}

	@Override
	protected Container initContainer() {
		AddressLazyQueryFactory factory = new AddressLazyQueryFactory(observer);
		// quite small batch size, for debugging. Don't use that in production.
		container = new AddressLazyQueryContainer(new LazyQueryDefinition(false, 2, null), factory);
		return container;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Address getAddressSelection() {
		Item item = container.getItem(((Property) addressTable).getValue());
		return ((BeanItem<Address>)item).getBean();
	}

	@Override
	public void setOrRefreshData(List<Address> addressList) {
		if (container != null) {
			container.refresh();
		}
	}

	@Override
	public void setObserver(Observer observer) {
		super.setObserver(observer);
		this.observer = observer;
	}
}
