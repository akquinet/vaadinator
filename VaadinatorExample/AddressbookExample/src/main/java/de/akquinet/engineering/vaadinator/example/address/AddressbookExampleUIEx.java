package de.akquinet.engineering.vaadinator.example.address;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import de.akquinet.engineering.vaadinator.example.address.dao.AddressDaoPlain;
import de.akquinet.engineering.vaadinator.example.address.dao.TeamDaoPlain;
import de.akquinet.engineering.vaadinator.example.address.service.AddressService;
import de.akquinet.engineering.vaadinator.example.address.service.AddressServicePlain;
import de.akquinet.engineering.vaadinator.example.address.service.TeamService;
import de.akquinet.engineering.vaadinator.example.address.service.TeamServicePlain;
import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactory;
import de.akquinet.engineering.vaadinator.example.address.ui.std.presenter.PresenterFactoryEx;
import de.akquinet.engineering.vaadinator.example.address.ui.std.view.VaadinViewFactoryEx;

public class AddressbookExampleUIEx extends AddressbookExampleUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		// for mobile, set yet another theme
		if (VaadinServlet.getCurrent() instanceof TouchKitServlet) {
			setTheme("touchkitexex");
		}
		super.init(request);
	}

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
			presenterFactory = new PresenterFactoryEx(new HashMap<String, Object>(), viewFactory,
					addressService, teamService);
			viewFactory.setPresenterFactory(presenterFactory);
		}
		return presenterFactory;
	}
}
