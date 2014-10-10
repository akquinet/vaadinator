/*
 * Copyright 2014 akquinet engineering GmbH
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
package de.akquinet.engineering.vaadinator.example.contractapplication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;

@WebServlet(initParams = { @WebInitParam(name = "UI", value = "de.akquinet.engineering.vaadinator.example.contractapplication.ContractApplicationExampleUI"),
		@WebInitParam(name = "productionMode", value = "false"), @WebInitParam(name = "disable-xsrf-protection", value = "true"), }, urlPatterns = "/*")
public class ContractApplicationExampleServlet extends TouchKitServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		TouchKitSettings s = getTouchKitSettings();
		s.getWebAppSettings().setWebAppCapable(true);
		s.getWebAppSettings().setStatusBarStyle("black");
	}
}
