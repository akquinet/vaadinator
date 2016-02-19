package org.vergien.vaadinator.example.webdriver;

import static com.github.webdriverextensions.Bot.assertThat;
import static com.github.webdriverextensions.Bot.open;
import static com.github.webdriverextensions.Bot.type;
import static org.hamcrest.Matchers.is;
import static org.vergien.vaadinator.example.webdriver.VaadinBot.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.vaadin.addonhelpers.automated.AbstractWebDriverCase;
import org.vergien.vaadinator.example.webdriver.ui.std.view.webdriver.page.AddressPage;

import com.github.webdriverextensions.junitrunner.WebDriverRunner;
import com.github.webdriverextensions.junitrunner.annotations.Firefox;

@RunWith(WebDriverRunner.class)
@Firefox
public class TestAddress extends AbstractWebDriverCase {
	private AddressPage addressPage;

	@Test
	public void addPerson() {
		// "http://localhost:9998/"
		open(BASEURL + "org.vergien.vaadinator.example.webdriver.WebDriverExampleDemo");
		waitForVaadin();
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());

		type("Daniel", addressPage.getAddressAddViewComponent().getVornameWebElement());
		type("Nordhoff-Vergien", addressPage.getAddressAddViewComponent().getNachnameWebElement());

		clickAndWait(addressPage.getAddressAddViewComponent().getSaveWebElement());

		assertThat(addressPage.getAddressListViewComponent().getaddressListTableRows().size(), is(1));

		assertThat(addressPage.getAddressListViewComponent().getaddressListTableRows().get(0).getNameCellWebElement()
				.getText(), is("Daniel Nordhoff-Vergien"));
	}
}
