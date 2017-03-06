package de.akquinet.engineering.vaadinator.example.address.ui.std.view;

import com.vaadin.ui.Component;

import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactory;
import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactoryAware;
import de.akquinet.engineering.vaadinator.example.address.ui.view.FieldInitializer;

public class CommonFieldInitializer implements FieldInitializer {
	private final PresenterFactory presenterFactory;

	public CommonFieldInitializer(PresenterFactory presenterFactory) {
		this.presenterFactory = presenterFactory;
	}

	@Override
	public void initializeField(Component field, Component view) {
		if (field instanceof PresenterFactoryAware) {
			((PresenterFactoryAware) field).setPresenterFactory(presenterFactory);
		}

	}
}
