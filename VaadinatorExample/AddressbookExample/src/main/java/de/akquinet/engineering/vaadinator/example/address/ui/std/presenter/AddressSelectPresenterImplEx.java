package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import java.util.Map;

import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressSelectView;

public class AddressSelectPresenterImplEx extends AddressSelectPresenterImpl {

	public AddressSelectPresenterImplEx(Map<String, Object> context, AddressSelectView view, AddressService service) {
		super(context, view, service);

	}

	@Override
	protected void loadFromModel() {
		getView().setOrRefreshData(null);
	}

	@Override
	public void startPresenting() {
		getView().setObserver(this);
		getView().initializeUi();
	}

}
