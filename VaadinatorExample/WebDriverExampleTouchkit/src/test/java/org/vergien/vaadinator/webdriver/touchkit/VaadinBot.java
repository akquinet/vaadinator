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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.vaadin.addonhelpers.automated.VaadinConditions;

import com.github.webdriverextensions.Bot;
import com.github.webdriverextensions.WebDriverExtensionFieldDecorator;
import com.github.webdriverextensions.WebDriverExtensionsContext;
import com.github.webdriverextensions.WebPage;
import com.github.webdriverextensions.internal.Openable;
import com.google.common.base.Predicate;

/**
 * {@link Bot} extensions to be used with vaadin
 * 
 * @author Daniel Nordhoff-Vergien
 *
 */
public class VaadinBot extends Bot {
	private static final int DEFUALT_WAIT_TIMEOUT = 30;

	private VaadinBot() {
		super();
	}

	public static void openAndWait(String url) {
		open(url);
		waitForVaadin();
	}

	public static void clickAndWait(WebElement webElement) {
		clickAndWait(webElement, DEFUALT_WAIT_TIMEOUT);
	}

	public static void clickAndWait(WebElement webElement, long secontsToWait) {
		Bot.click(webElement);
		waitForVaadin(secontsToWait);
	}

	public static void doubleClick(WebElement webElement) {
		Actions action = new Actions(driver());
		action.doubleClick(webElement).perform();
	}

	public static void doubleClickAndWait(WebElement webElement) {
		doubleClickAndWait(webElement, DEFUALT_WAIT_TIMEOUT);
	}

	public static void doubleClickAndWait(WebElement webElement, long secondsToWait) {
		doubleClick(webElement);
		waitForVaadin(secondsToWait);
	}

	public static void waitForVaadin() {
		waitForVaadin(DEFUALT_WAIT_TIMEOUT);
	}

	public static void waitForVaadin(long secondsToWait) {
		waitUntil(VaadinConditions.ajaxCallsCompleted(), secondsToWait);
	}

	public static void waitUnitl(Predicate<WebDriver> perdicate) {
		waitUntil(perdicate, DEFUALT_WAIT_TIMEOUT);
	}

	public static void waitUntil(Predicate<WebDriver> perdicate, long secondsToWait) {
		new WebDriverWait(WebDriverExtensionsContext.getDriver(), secondsToWait).until(perdicate);
	}

	public static void clickAndInitialize(WebElement buttonElement, Openable openable) {
		clickAndWait(buttonElement);
		PageFactory.initElements(new WebDriverExtensionFieldDecorator(WebDriverExtensionsContext.getDriver()),
				openable);
		openable.assertIsOpen(new Object[0]);
	}

	public static <T extends WebPage> T clickAndInitialize(WebElement buttonElement, Class<T> webPage) {
		clickAndWait(buttonElement);
		T page = initialize(webPage);
		return page;
	}

	public static <T extends WebPage> T initialize(Class<T> webPage) {
		T page = initElements(webPage);
		page.assertIsOpen();
		return page;
	}

	public static <T extends WebPage> T initElements(Class<T> pageClass) {
		try {
			T page = pageClass.newInstance();
			page.initElements();
			return page;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
