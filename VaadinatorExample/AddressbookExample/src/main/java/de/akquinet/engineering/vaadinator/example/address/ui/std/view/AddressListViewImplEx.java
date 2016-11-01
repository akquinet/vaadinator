package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import java.util.List;

import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;

import com.vaadin.data.Container;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.ui.view.ExceptionMappingStrategy;

public class AddressListViewImplEx extends AddressListViewImpl {

	private AddressListView.Observer observer;
	private AddressContainer container;

	public AddressListViewImplEx(ExceptionMappingStrategy exceptionMappingStrategy) {
		super(exceptionMappingStrategy);
	}
@Override
protected Container initContainer() {

	AddressLazyQueryFactory factory = new AddressLazyQueryFactory(observer);
	container = new AddressContainer(
			new LazyQueryDefinition(false, 50, null), factory);
	return container;
}
	// @Override
	// public void initializeUi() {
	// super.initializeUi();
	//
	// addressTable.setContainerDataSource(container);
	//
	// List<String> visibleCols = new ArrayList<String>();
	// visibleCols.add("geburtsdatum");
	// visibleCols.add("name");
	// visibleCols.add("email");
	// addressTable.setColumnHeader("geburtsdatum",
	// obtainBundle().getString("entity.Address.property.geburtsdatum"));
	// addressTable.setColumnHeader("name",
	// obtainBundle().getString("entity.Address.property.name"));
	// addressTable.setColumnHeader("email",
	// obtainBundle().getString("entity.Address.property.email"));
	// }

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
