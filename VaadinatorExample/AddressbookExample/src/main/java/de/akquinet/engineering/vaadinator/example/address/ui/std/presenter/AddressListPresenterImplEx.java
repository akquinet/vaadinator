package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import java.util.Map;

import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.ui.presenter.SubviewCapablePresenter;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.AddressListView;

public class AddressListPresenterImplEx extends AddressListPresenterImpl {

	public AddressListPresenterImplEx(Map<String, Object> context, AddressListView view,
			PresenterFactory presenterFactory, AddressService service,
			SubviewCapablePresenter subviewCapablePresenter) {
		super(context, view, presenterFactory, service, subviewCapablePresenter);

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
