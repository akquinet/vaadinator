package de.akquinet.engineering.vaadinator.example.address;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.vaadin.annotations.Widgetset;

import de.akquinet.engineering.vaadinator.example.address.dao.AddressDaoPlain;
import de.akquinet.engineering.vaadinator.example.address.dao.TeamDaoPlain;
import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.service.AddressServicePlain;
import de.akquinet.engineering.vaadinator.example.address.service.TeamService;
import de.akquinet.engineering.vaadinator.example.address.service.TeamServicePlain;
import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactory;
import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactoryEx;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.VaadinViewFactoryEx;

@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class AddressbookExampleUIEx extends AddressbookExample8UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected PresenterFactory obtainPresenterFactory(String contextPath) {
		if (presenterFactory == null) {
			// simple, overwrite method for e.g. Spring / CDI / ...
			// Entity-Manager NUR Thread-Safe, wenn er injected wird wie hier
			AddressService addressService;
			TeamService teamService;
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("AddressbookExample");
			AddressDaoPlain addressDaoPlain = new AddressDaoPlain(entityManagerFactory);
			addressService = new AddressServicePlain(entityManagerFactory, addressDaoPlain);
			TeamDaoPlain teamDaoPlain = new TeamDaoPlain(entityManagerFactory);
			teamService = new TeamServicePlain(entityManagerFactory, teamDaoPlain);
			VaadinViewFactoryEx viewFactory = new VaadinViewFactoryEx();
			presenterFactory = new PresenterFactoryEx(new HashMap<String, Object>(), viewFactory, addressService,
					teamService);
			viewFactory.setPresenterFactory(presenterFactory);
		}
		return presenterFactory;
	}
}
