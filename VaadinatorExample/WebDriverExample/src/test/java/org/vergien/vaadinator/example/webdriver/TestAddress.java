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
		open(BASEURL + "org.vergien.vaadinator.example.webdriver.WebDriverExampleDemo");
		waitForVaadin();
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());

		type("Daniel", addressPage.getAddressAddViewComponent().getVornameWebElement());
		type("Nordhoff-Vergien", addressPage.getAddressAddViewComponent().getNachnameWebElement());

		addressPage.getAddressAddViewComponent().getAnredeVaadinComboBox().selectItemFromFilter(0);
		clickAndWait(addressPage.getAddressAddViewComponent().getSaveWebElement());

		assertThat(addressPage.getAddressListViewComponent().getAddressListTableRows().size(), is(1));

		assertThat(addressPage.getAddressListViewComponent().getAddressListTableRows().get(0).getNameCellWebElement()
				.getText(), is("Daniel Nordhoff-Vergien"));

		clickAndWait(addressPage.getAddressListViewComponent().getAddressListTableRows().get(0));
			
		assertValueEquals("Daniel", addressPage.getAddressChangeViewComponent().getVornameWebElement());
		assertValueEquals("Nordhoff-Vergien", addressPage.getAddressChangeViewComponent().getNachnameWebElement());
		assertThat(addressPage.getAddressChangeViewComponent().getAnredeVaadinComboBox().getValue(), is("Herr"));
	}
}
