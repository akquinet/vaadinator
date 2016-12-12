/*
 * Copyright 2016 Daniel Nordhoff-Vergien
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vergien.vaadinator.webdriver.touchkit;

import static com.github.webdriverextensions.Bot.assertIsOpen;
import static com.github.webdriverextensions.Bot.assertThat;
import static com.github.webdriverextensions.Bot.assertValueEquals;
import static com.github.webdriverextensions.Bot.open;
import static com.github.webdriverextensions.Bot.type;
import static com.github.webdriverextensions.vaadin.VaadinBot.clickAndWait;
import static com.github.webdriverextensions.vaadin.VaadinBot.doubleClickAndWait;
import static com.github.webdriverextensions.vaadin.VaadinBot.waitForVaadin;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.interactions.Actions;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.webdriver.component.AddressListViewComponent.AddressListTableRowComponent;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.webdriver.page.AddressAddPage;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.webdriver.page.AddressChangePage;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.webdriver.page.AddressListPage;
import org.vergien.vaadinator.webdriver.touchkit.ui.std.view.webdriver.page.FirstPageViewPage;

import com.github.webdriverextensions.Bot;
import com.github.webdriverextensions.junitrunner.WebDriverRunner;
import com.github.webdriverextensions.junitrunner.annotations.PhantomJS;
import com.github.webdriverextensions.vaadin.VaadinBot;

@RunWith(WebDriverRunner.class)
@PhantomJS
public class TestAddressIT extends AbstractWebdriverTest {
	private FirstPageViewPage firstPageViewPage;
	private AddressAddPage addressAddPage;
	private AddressListPage addressListPage;
	private AddressChangePage addressChangePage;

	@Before
	public void openPage() {
		open(BASEURL + CompleteDemo.class.getName());
		waitForVaadin();
		assertIsOpen(firstPageViewPage);
	}

	@Test
	public void addAddress() {
		clickAndWait(firstPageViewPage.getFirstPageViewComponent().getNewAddressWebElement());
		assertIsOpen(addressAddPage);

		type("Daniel", addressAddPage.getAddressAddViewComponent().getVornameWebElement());
		String nachname = UUID.randomUUID().toString();
		type(nachname, addressAddPage.getAddressAddViewComponent().getNachnameWebElement());

		Actions action = new Actions(VaadinBot.driver());
		action.doubleClick(addressAddPage.getAddressAddViewComponent().getAnredeWebElement()).perform();

		// TODO VaadinComboBox not working for touchkit yet
		// addressAddPage.getAddressAddViewComponent().getAnredeVaadinComboBox().selectItemFromFilter(0);

		clickAndWait(addressAddPage.getAddressAddViewComponent().getSaveWebElement());
		clickAndWait(firstPageViewPage.getFirstPageViewComponent().getListAddressWebElement());
		
		boolean foundInTable = false;
		for (AddressListTableRowComponent row : addressListPage.getAddressListViewComponent()
				.getAddressListTableRows()) {
			if (row.getNameCellWebElement().getText().equals("Daniel " + nachname)) {
				foundInTable = true;
				doubleClickAndWait(row);
			}
		}
		assertThat("Generated address not found in table", foundInTable, is(true));
		assertValueEquals("Daniel", addressChangePage.getAddressChangeViewComponent().getVornameWebElement());
		assertValueEquals(nachname, addressChangePage.getAddressChangeViewComponent().getNachnameWebElement());
	//	assertThat(addressChangePage.getAddressChangeViewComponent().getAnredeVaadinComboBox().getValue(), is("Herr"));
	}

	@Test
	public void cancelAddingPerson() {
		clickAndWait(firstPageViewPage.getFirstPageViewComponent().getNewAddressWebElement());
		assertIsOpen(addressAddPage);

		clickAndWait(addressAddPage.getAddressAddViewComponent().getCancelWebElement());

		// Wait for the touchkit animation
		Bot.waitFor(1);
		
		assertIsOpen(firstPageViewPage);
	}
}
