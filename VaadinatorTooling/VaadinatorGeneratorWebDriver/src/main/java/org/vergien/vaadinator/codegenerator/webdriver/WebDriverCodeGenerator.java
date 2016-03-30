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
package org.vergien.vaadinator.codegenerator.webdriver;

import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.*;

import java.io.IOException;

import de.akquinet.engineering.vaadinator.generator.CodeGenerator;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public class WebDriverCodeGenerator implements CodeGenerator {
	private static final String TEMPLATE_PACKAGE = "/org/vergien/vaadinator/templates/webdriver/";
	private static final String DEFAULT_TEMPLATE_PACKAGE = "/de/akquinet/engineering/vaadinator/templates/";

	@Override
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws Exception {

		vaadinatorConfig.getLog().info("Generating WebDriver PageObjects");
		if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.SOURCES
				|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
			if (vaadinatorConfig.isHasDisplayBeans()) {
				generateFirstPageComponents(vaadinatorConfig);
				for (BeanDescription desc : vaadinatorConfig.getBeanDescriptions()) {
					if (desc.isDisplayed()) {
						for (DisplayProfileDescription p : desc.getDisplayProfiles()) {
							String componentPckg = desc.getViewPckg(p) + ".webdriver.component";
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"EditViewComponent.template",
									packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(), componentPckg,
											desc.getClassName(), "EditViewComponent.java"),
									TEMPLATE_PACKAGE);
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"AddViewComponent.template",
									packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(), componentPckg,
											desc.getClassName(), "AddViewComponent.java"),
									TEMPLATE_PACKAGE);
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"ChangeViewComponent.template",
									packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(), componentPckg,
											desc.getClassName(), "ChangeViewComponent.java"),
									TEMPLATE_PACKAGE);
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"ListViewComponent.template",
									packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(), componentPckg,
											desc.getClassName(), "ListViewComponent.java"),
									TEMPLATE_PACKAGE);
							if (isGeneratePages(vaadinatorConfig)) {
								String pagePckg = desc.getViewPckg(p) + ".webdriver.page";
								runVelocity(desc, vaadinatorConfig.getCommonMap(), pagePckg, desc.getPckg(),
										desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
										"Page.template", packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(),
												pagePckg, desc.getClassName(), "Page.java"),
										TEMPLATE_PACKAGE);
							}
						}
					}
				}
			}
		}

	}

	private boolean isGeneratePages(VaadinatorConfig vaadinatorConfig) {
		boolean generatePages = true;
		if ("false".equalsIgnoreCase(vaadinatorConfig.getGeneratorOptions().get("webDriverPages"))) {
			generatePages = false;
		}
		return generatePages;
	}

	private void generateFirstPageComponents(VaadinatorConfig vaadinatorConfig) throws IOException {
		if (isTemplateExisting("FirstPageViewImpl.template", DEFAULT_TEMPLATE_PACKAGE)) {
			for (String displayProfileName : vaadinatorConfig.getDisplayProfileNames()) {
				String viewPckg = vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".view";
				String webdriverComponentPckg = viewPckg + ".webdriver.component";
				runVelocity(null, vaadinatorConfig.getCommonMap(), webdriverComponentPckg, null,
						vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", viewPckg,
						displayProfileName, "FirstPageViewComponent.template",
						packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(), webdriverComponentPckg,
								"FirstPageViewComponent", ".java"),
						false, TEMPLATE_PACKAGE);
				if (isGeneratePages(vaadinatorConfig)) {
					String webdriverPagePckg = viewPckg + ".webdriver.page";
					runVelocity(null, vaadinatorConfig.getCommonMap(), webdriverPagePckg, null,
							vaadinatorConfig.getBasePckg() + ".ui." + displayProfileName + ".presenter", viewPckg,
							displayProfileName, "FirstPageViewPage.template",
							packageToFile(vaadinatorConfig.getTargetFolderTestSrcStart(), webdriverPagePckg,
									"FirstPageViewPage", ".java"),
							false, TEMPLATE_PACKAGE);
				}

			}
		}
	}
}
