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

import org.vaadin.addonhelpers.TServer;

import com.vaadin.annotations.Widgetset;

@Widgetset("org.vergien.vaadinator.webdriver.touchkit.MobileWidgetset")
public class Demo extends TServer {

	public Demo(String string) {
		super(string);
	}

	public static void main(String[] args) throws Exception {
		Demo demo = new Demo("target/WebDriverExampleTouchkit-0.20-SNAPSHOTstage");
		demo.startServer();
	}
}
