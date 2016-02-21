package de.akquinet.engineering.vaadinator.example.address;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

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

}
