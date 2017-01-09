package de.akquinet.engineering.vaadinator.example.address.ui.std.presenter;

import java.util.Map;

import com.vaadin.ui.Notification;

import de.akquinet.engineering.vaadinator.example.address.model.Address;
import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.service.TeamService;
import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.AddressSelectPresenter.BeforeAddressSelectListener;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.ViewFactory;

public class PresenterFactoryEx extends PresenterFactory {

	public PresenterFactoryEx(Map<String, Object> context, ViewFactory viewFactory, AddressService addressService,
			TeamService teamService) {
		super(context, viewFactory, addressService, teamService);
		commonAfterSaveListener = new CommonAfterSaveListener();
		commonAfterCancelListener = new CommonAfterCancelListener();
		commonAfterDeleteListener = new CommonAfterDeleteListener();
	}

	@Override
	public AddressSelectPresenter createAddressSelectPresenter() {
		AddressSelectPresenter selectPresenter = super.createAddressSelectPresenter();
		selectPresenter.addBeforeSelectListener(new BeforeAddressSelectListener() {
			@Override
			public void beforeSelect(Address entity) {
				Notification.show("Addresse ausgewählt");
			}
		});
		return selectPresenter;
	}

}
