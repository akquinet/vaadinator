package org.vergien.vaadinator.webdriver.touchkit;

import org.vergien.vaadinator.webdriver.touchkit.ui.presenter.Presenter;

public class AddAdressDemo extends AbstractDemo {

	@Override
	Presenter getPresenter() {
		return obtainPresenterFactory().createAddressAddPresenter(null);
	}

}
