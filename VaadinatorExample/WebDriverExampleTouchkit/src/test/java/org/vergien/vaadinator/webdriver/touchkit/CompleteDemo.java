package org.vergien.vaadinator.webdriver.touchkit;

import org.vergien.vaadinator.webdriver.touchkit.ui.presenter.Presenter;

@SuppressWarnings("serial")
public class CompleteDemo extends AbstractDemo {

	@Override
	Presenter getPresenter() {
		return obtainPresenterFactory().createFirstPagePresenter();
	}

}
