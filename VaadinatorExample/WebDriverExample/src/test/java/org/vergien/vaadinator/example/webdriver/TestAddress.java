package org.vergien.vaadinator.example.webdriver;

import static com.github.webdriverextensions.Bot.assertIsNotDisplayed;
import static com.github.webdriverextensions.Bot.assertIsOpen;
import static com.github.webdriverextensions.Bot.assertThat;
import static com.github.webdriverextensions.Bot.assertValueEquals;
import static com.github.webdriverextensions.Bot.open;
import static com.github.webdriverextensions.Bot.type;
import static org.hamcrest.Matchers.*;
import static org.vergien.vaadinator.example.webdriver.VaadinBot.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vaadin.addonhelpers.automated.AbstractWebDriverCase;
import org.vergien.vaadinator.example.webdriver.ui.std.view.webdriver.component.AddressListViewComponent.AddressListTableRowComponent;
import org.vergien.vaadinator.example.webdriver.ui.std.view.webdriver.page.AddressPage;

import com.github.webdriverextensions.junitrunner.WebDriverRunner;
import com.github.webdriverextensions.junitrunner.annotations.Firefox;

@RunWith(WebDriverRunner.class)
@Firefox
public class TestAddress extends AbstractWebDriverCase {
	private AddressPage addressPage;

	@Before
	public void openPage() {
		open(BASEURL + "org.vergien.vaadinator.example.webdriver.WebDriverExampleDemo");
		waitForVaadin();
		assertIsOpen(addressPage);
	}

	@Test
	public void addPerson() {
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());

		type("Daniel", addressPage.getAddressAddViewComponent().getVornameWebElement());
		String nachname = UUID.randomUUID().toString();
		type(nachname, addressPage.getAddressAddViewComponent().getNachnameWebElement());

		addressPage.getAddressAddViewComponent().getAnredeVaadinComboBox().selectItemFromFilter(0);
		clickAndWait(addressPage.getAddressAddViewComponent().getSaveWebElement());

		boolean foundInTable = false;
		for (AddressListTableRowComponent row : addressPage.getAddressListViewComponent().getAddressListTableRows()) {
			if (row.getNameCellWebElement().getText().equals("Daniel " + nachname)) {
				foundInTable = true;
				clickAndWait(row);
			}
		}
		assertThat("Generated address not found in table", foundInTable, is(true));

		assertValueEquals("Daniel", addressPage.getAddressChangeViewComponent().getVornameWebElement());
		assertValueEquals(nachname, addressPage.getAddressChangeViewComponent().getNachnameWebElement());
		assertThat(addressPage.getAddressChangeViewComponent().getAnredeVaadinComboBox().getValue(), is("Herr"));
	}

	@Test
	public void cancelAddingPerson() {
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());

		clickAndWait(addressPage.getAddressAddViewComponent().getCancelWebElement());

		assertIsNotDisplayed(addressPage.getAddressAddViewComponent());
	}

	@Test
	public void searchPerson() {
		clickAndWait(addressPage.getAddressListViewComponent().getAddAddressWebElement());
		String nachName = UUID.randomUUID().toString();
		type("Daniel", addressPage.getAddressAddViewComponent().getVornameWebElement());
		type(nachName, addressPage.getAddressAddViewComponent().getNachnameWebElement());
		clickAndWait(addressPage.getAddressAddViewComponent().getSaveWebElement());

		type("Hans", addressPage.getAddressListViewComponent().getSearchFieldWebElement());
		waitForVaadin();

		List<AddressListTableRowComponent> rows = addressPage.getAddressListViewComponent().getAddressListTableRows();
		for (AddressListTableRowComponent row : rows) {
			assertThat(row.getNameCellWebElement().getText(), is(not("Daniel " + nachName)));
		}

		clearAndType(nachName, addressPage.getAddressListViewComponent().getSearchFieldWebElement());
		assertThat(addressPage.getAddressListViewComponent().getAddressListTableRows().size(), is(1));

		assertTextEquals("Daniel " + nachName,
				(addressPage.getAddressListViewComponent().getAddressListTableRows().get(0).getNameCellWebElement()));
	}
}
