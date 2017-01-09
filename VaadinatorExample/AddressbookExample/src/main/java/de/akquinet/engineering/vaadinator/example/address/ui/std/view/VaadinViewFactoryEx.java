package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactory;

public class VaadinViewFactoryEx extends VaadinViewFactory {

	public VaadinViewFactoryEx() {
		super();
		this.buttonFactory = new ButtonFactoryImpl();
	}
	public void setPresenterFactory(PresenterFactory presenterFactory) {
		commonFieldInitializer = new CommonFieldInitializer(presenterFactory);
	}

}
