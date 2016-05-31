/*
 * Copyright 2014, 2016 Daniel Norhoff-Vergien and akquinet engineering GmbH
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
package de.akquinet.engineering.vaadinator.generator;

import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.existingFolder;
import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.packageToFile;
import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.runVelocity;

import java.io.File;
import java.io.IOException;

import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public class DefaultCodeGenerator implements CodeGenerator {

	private static final String TEMPLATE_PACKAGE = "/de/akquinet/engineering/vaadinator/templates/";

	@Override
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws IOException {
		File targetFolderWebStart = existingFolder(vaadinatorConfig.getTargetFolderBaseStart(), "webapp");

		if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.RESOURCES
				|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
			// bei Resources bisher nur common
			File targetFolderWebInf = existingFolder(targetFolderWebStart, "WEB-INF");
			File targetFolderVaadin = existingFolder(targetFolderWebStart, "VAADIN");
			File targetFolderThemes = existingFolder(targetFolderVaadin, "themes");
			File targetFolderTouchkitex = existingFolder(targetFolderThemes, "touchkitex");

			if (vaadinatorConfig.isHasDisplayBeans() && vaadinatorConfig.isGenServletBase()) {
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "stylescss.template", (new File(targetFolderTouchkitex, "styles.css")), false,
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "touchkitexcss.template", (new File(targetFolderTouchkitex, "touchkitex.css")), false,
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "beansxml.template", (new File(targetFolderWebInf, "beans.xml")), false,
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "webxml.template", (new File(targetFolderWebInf, "web.xml")), false, TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "widgetset.template",
						packageToFile(vaadinatorConfig.getTargetFolderResStart(), vaadinatorConfig.getBasePckg(),
								vaadinatorConfig.getProjectName() + "Widgetset", ".gwt.xml"),
						false, TEMPLATE_PACKAGE);
			}
			if (vaadinatorConfig.isHasDisplayBeans()) {
				// Internationalization
				// EINE pro Profile
				for (String displayProfileName : vaadinatorConfig.getDisplayProfileNames()) {
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"messages.template",
							packageToFile(vaadinatorConfig.getTargetFolderResStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", "messages",
									".properties"),
							TEMPLATE_PACKAGE);
				}
			}
		}
		if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.SOURCES
				|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
			// jetzt common generieren
			if (vaadinatorConfig.isHasDisplayBeans()) {
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.presenter",
						null, null, null, null, "Presenter.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.presenter", "Presenter", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.presenter",
						null, null, null, null, "SubviewCapablePresenter.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.presenter", "SubviewCapablePresenter", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "View.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "View", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "ObservableView.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "ObservableView", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "ValidatableView.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "ValidatableView", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "ErrorHandlerView.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "ErrorHandlerView", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "ExceptionMappingStrategy.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "ExceptionMappingStrategy", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "DefaultExceptionMappingStrategy.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "DefaultExceptionMappingStrategy",
								".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "FieldInitializer.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "FieldInitializer",
								".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg() + ".ui.view", null,
						null, null, null, "VaadinView.template",
						packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg() + ".ui.view", "VaadinView",
								".java"),
						TEMPLATE_PACKAGE);
			}
			// auch für Service-Layer
			if (vaadinatorConfig.isHasDisplayBeans() || vaadinatorConfig.isHasServiceBeans()) {
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "BusinessException.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg(), "BusinessException", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null, "TechnicalException.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg(), "TechnicalException", ".java"),
						TEMPLATE_PACKAGE);
			}
			// wenn wir Basis generieren wollen auch das Folgende:
			if (vaadinatorConfig.isHasDisplayBeans() && vaadinatorConfig.isGenServletBase()) {
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null,
						"Servlet.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg(), vaadinatorConfig.getProjectName() + "Servlet", ".java"),
						TEMPLATE_PACKAGE);
				runVelocity(null, vaadinatorConfig.getCommonMap(), vaadinatorConfig.getBasePckg(), null, null, null,
						null,
						"UI.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
								vaadinatorConfig.getBasePckg(), vaadinatorConfig.getProjectName() + "UI", ".java"),
						TEMPLATE_PACKAGE);
			}
			// dazu gehoert auch die Startseite und die Factories
			// EINE pro Profile
			if (vaadinatorConfig.isHasDisplayBeans()) {
				for (String displayProfileName : vaadinatorConfig.getDisplayProfileNames()) {
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"PresenterFactory.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
									"PresenterFactory", ".java"),
							TEMPLATE_PACKAGE);

					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"FirstPagePresenter.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
									"FirstPagePresenter", ".java"),
							TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"MainPresenter.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
									"MainPresenter", ".java"),
							TEMPLATE_PACKAGE);

					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"FirstPagePresenterImpl.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
									"FirstPagePresenterImpl", ".java"),
							TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"MainPresenterImpl.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
									"MainPresenterImpl", ".java"),
							TEMPLATE_PACKAGE);

					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"ViewFactory.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view",
									"ViewFactory", ".java"),
							TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"VaadinViewFactory.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view",
									"VaadinViewFactory", ".java"),
							TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"FirstPageView.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view",
									"FirstPageView", ".java"),
							TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"FirstPageViewImpl.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view",
									"FirstPageViewImpl", ".java"),
							false, TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"MainView.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", "MainView",
									".java"),
							TEMPLATE_PACKAGE);
					runVelocity(null, vaadinatorConfig.getCommonMap(),
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter",
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view", displayProfileName,
							"MainViewImpl.template",
							packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view",
									"MainViewImpl", ".java"),
							false, TEMPLATE_PACKAGE);
				}
			}
			// Bean-spezifisch
			for (BeanDescription desc : vaadinatorConfig.getBeanDescriptions()) {
				if (desc.isMapped()) {
					runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPckg(), desc.getPckg(), null, null, null,
							"Mapper.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getPckg(),
									desc.getClassName(), "Mapper.java"),
							TEMPLATE_PACKAGE);
				}
				if (desc.isDisplayed()) {
					// Presenter, Views
					for (DisplayProfileDescription p : desc.getDisplayProfiles()) {
						// Edit-Presenter
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"EditPresenter.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "EditPresenter.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"EditPresenterImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "EditPresenterImpl.java"),
								TEMPLATE_PACKAGE);
						// Edit-View
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(), "EditView.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getViewPckg(p),
										desc.getClassName(), "EditView.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"EditViewImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "EditViewImpl.java"),
								TEMPLATE_PACKAGE);
						// Add-Presenters
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"AddPresenter.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "AddPresenter.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"AddPresenterImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "AddPresenterImpl.java"),
								TEMPLATE_PACKAGE);
						// Add-View
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(), "AddView.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getViewPckg(p),
										desc.getClassName(), "AddView.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"AddViewImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "AddViewImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ChangePresenter.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "ChangePresenter.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ChangePresenterImpl.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getPresenterPckg(p),
										desc.getClassName(), "ChangePresenterImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ListPresenter.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "ListPresenter.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ListPresenterImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "ListPresenterImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"SelectPresenter.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "SelectPresenter.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPresenterPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"SelectPresenterImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getPresenterPckg(p), desc.getClassName(), "SelectPresenterImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(), "AddView.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getViewPckg(p),
										desc.getClassName(), "AddView.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ChangeView.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "ChangeView.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ChangeViewImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "ChangeViewImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(), "ListView.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getViewPckg(p),
										desc.getClassName(), "ListView.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"ListViewImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "ListViewImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(), "SelectView.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getViewPckg(p),
										desc.getClassName(), "SelectView.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"SelectViewImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "SelectViewImpl.java"),
								TEMPLATE_PACKAGE);
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getViewPckg(p), desc.getPckg(),
								desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
								"SelectField.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getViewPckg(p), desc.getClassName(), "SelectField.java"),
								TEMPLATE_PACKAGE);
					}
				}
				if (desc.isDisplayed() || desc.isService()) {
					// Service
					runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".service", desc.getPckg(),
							null, null, null,
							"Service.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									desc.getBasePckg() + ".service", desc.getClassName(), "Service.java"),
							TEMPLATE_PACKAGE);
					if (vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.EJB
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.PLAIN
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.ALL) {
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".service",
								desc.getPckg(), null, null, null, "ServiceImpl.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getBasePckg() + ".service", desc.getClassName(), "ServiceImpl.java"),
								TEMPLATE_PACKAGE);
					}
					if (vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.EJB
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.ALL) {
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".service",
								desc.getPckg(), null, null, null, "ServiceEjb.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getBasePckg() + ".service", desc.getClassName(), "ServiceEjb.java"),
								TEMPLATE_PACKAGE);
					}
					if (vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.PLAIN
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.ALL) {
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".service",
								desc.getPckg(), null, null, null, "ServicePlain.template",
								packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getBasePckg() + ".service", desc.getClassName(), "ServicePlain.java"),
								TEMPLATE_PACKAGE);
					}
					// ditto Dao
					runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".dao", desc.getPckg(),
							null, null, null, "Dao.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									desc.getBasePckg() + ".dao", desc.getClassName(), "Dao.java"),
							TEMPLATE_PACKAGE);
					if (vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.EJB
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.PLAIN
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.ALL) {
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".dao", desc.getPckg(),
								null, null, null,
								"DaoImpl.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getBasePckg() + ".dao", desc.getClassName(), "DaoImpl.java"),
								TEMPLATE_PACKAGE);
					}
					if (vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.EJB
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.ALL) {
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".dao", desc.getPckg(),
								null, null, null,
								"DaoEjb.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getBasePckg() + ".dao", desc.getClassName(), "DaoEjb.java"),
								TEMPLATE_PACKAGE);
					}
					if (vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.PLAIN
							|| vaadinatorConfig.getArtifactTypeEn() == VaadinatorConfig.ArtifactType.ALL) {
						runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getBasePckg() + ".dao", desc.getPckg(),
								null, null, null,
								"DaoPlain.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
										desc.getBasePckg() + ".dao", desc.getClassName(), "DaoPlain.java"),
								TEMPLATE_PACKAGE);
					}
				}
				if (desc.isWrapped()) {
					runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPckg(), desc.getPckg(), null, null, null,
							"Wrapper.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									desc.getPckg(), desc.getClassName(), "Wrapper.java"),
							TEMPLATE_PACKAGE);
				}
				// for all having at least one
				if (desc.isDisplayed() || desc.isMapped() || desc.isService() || desc.isWrapped()) {
					runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPckg(), desc.getPckg(), null, null, null,
							"Properties.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
									desc.getPckg(), desc.getClassName(), "Properties.java"),
							TEMPLATE_PACKAGE);
					runVelocity(desc, vaadinatorConfig.getCommonMap(), desc.getPckg(), desc.getPckg(), null, null, null,
							"Query.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), desc.getPckg(),
									desc.getClassName(), "Query.java"),
							TEMPLATE_PACKAGE);
				}
			}
		}
	}
}
