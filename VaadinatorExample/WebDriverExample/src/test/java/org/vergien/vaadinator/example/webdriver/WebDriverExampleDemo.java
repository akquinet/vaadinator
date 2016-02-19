package org.vergien.vaadinator.example.webdriver;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.vaadin.addonhelpers.AbstractTest;
import org.vergien.vaadinator.example.webdriver.dao.AddressDaoPlain;
import org.vergien.vaadinator.example.webdriver.service.AddressService;
import org.vergien.vaadinator.example.webdriver.service.AddressServicePlain;
import org.vergien.vaadinator.example.webdriver.ui.std.presenter.MainPresenter;
import org.vergien.vaadinator.example.webdriver.ui.std.presenter.PresenterFactory;
import org.vergien.vaadinator.example.webdriver.ui.std.view.MainView;
import org.vergien.vaadinator.example.webdriver.ui.std.view.VaadinViewFactory;

import com.vaadin.ui.Component;

public class WebDriverExampleDemo extends AbstractTest {

	PresenterFactory presenterFactory = null;

	@Override
	public Component getTestComponent() {
		MainPresenter mpres;
		mpres = obtainPresenterFactory().createMainPresenter();
		MainView mview = mpres.getView();
		// and go
		mpres.startPresenting();
		return (Component) mview.getComponent();
	}

	protected PresenterFactory obtainPresenterFactory() {
		if (presenterFactory == null) {
			AddressService addressService;
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("WebDriverExample");
			AddressDaoPlain addressDaoPlain = new AddressDaoPlain(entityManagerFactory);
			addressService = new AddressServicePlain(entityManagerFactory, addressDaoPlain);
			
			presenterFactory = new PresenterFactory(new HashMap<String, Object>(), new VaadinViewFactory(),
					addressService);
		}
		return presenterFactory;
	}
}
