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
package de.akquinet.engineering.vaadinator.generator.te;

import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.existingFolder;
import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.runVelocity;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import de.akquinet.engineering.vaadinator.generator.CodeGenerator;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public class TesteditorDesktopGenerator implements CodeGenerator {
	private static final String TEMPLATE_PACKAGE = "/de/akquinet/engineering/vaadinator/templates/test/";

	@Override
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws Exception {
		
		Log log = vaadinatorConfig.getLog();
		log.info("Generating testeditor desktop artifacts");

		if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.SOURCES
				|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
			if (vaadinatorConfig.isHasDisplayBeans()) {
				File targetFolderTesteditStart = existingFolder(vaadinatorConfig.getTargetFolderBaseStart(),
						"testeditor");
				File tests = existingFolder(targetFolderTesteditStart, vaadinatorConfig.getProjectName() + "Tests");
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"AllActionGroups.template", new File(tests, "AllActionGroups.xml"), TEMPLATE_PACKAGE, log);
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"ConfigTpr.template", new File(tests, "config.tpr"), TEMPLATE_PACKAGE, log);
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"TechnicalBindingTypeCollection.template",
						new File(tests, "TechnicalBindingTypeCollection.xml"), TEMPLATE_PACKAGE, log);
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"ElementList.template", new File(tests, "ElementList.conf"), TEMPLATE_PACKAGE, log);
				File fitnesseRoot = existingFolder(tests, "FitNesseRoot");
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"FitnesseRootProperties.template", new File(fitnesseRoot, "properties"), TEMPLATE_PACKAGE, log);
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"FitnesseRootUpdateDoNotCopyOverList.template",
						new File(fitnesseRoot, "updateDoNotCopyOverList"), TEMPLATE_PACKAGE, log);
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"FitnesseRootUpdateList.template", new File(fitnesseRoot, "updateList"), TEMPLATE_PACKAGE, log);
				File projectTests = existingFolder(fitnesseRoot, vaadinatorConfig.getProjectName() + "Tests");
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"TestContent.template", new File(projectTests, "content.txt"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().put("prop", new String[] { "Edit", "Files", "Properties",
						"RecentChanges", "Refactor", "Search", "Versions", "WhereUsed" });
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"Properties.template", new File(projectTests, "properties.xml"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().remove("prop");
				File scenarioLibrary = existingFolder(projectTests, "ScenarioLibrary");
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"ScenarioLibraryContent.template", new File(scenarioLibrary, "content.txt"),
						TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().put("prop", new String[] { "Edit", "Files", "Properties",
						"RecentChanges", "Refactor", "Search", "Versions", "WhereUsed" });
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"Properties.template", new File(scenarioLibrary, "properties.xml"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().remove("prop");
				File testSzenarien = existingFolder(projectTests, "TestSzenarien");
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"TestSzenarienContent.template", new File(testSzenarien, "content.txt"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().put("prop", new String[] { "Edit", "Files", "Properties",
						"RecentChanges", "Refactor", "Search", "Versions", "WhereUsed" });
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"Properties.template", new File(testSzenarien, "properties.xml"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().remove("prop");
				File frontPage = existingFolder(fitnesseRoot, "FrontPage");
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"FrontPageContent.template", new File(frontPage, "content.txt"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap()
						.put("prop",
								new String[] { "AddChild", "Edit", "Files", "Help", "Properties", "Prune",
										"RecentChanges", "Refactor", "Search", "Static", "Suites", "Versions",
										"WhereUsed" });
				runVelocity(null, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
						"Properties.template", new File(frontPage, "properties.xml"), TEMPLATE_PACKAGE, log);
				vaadinatorConfig.getCommonMap().remove("prop");
				// Bean-spezifisch
				for (BeanDescription desc : vaadinatorConfig.getBeanDescriptions()) {
					if (desc.isDisplayed()) {
						File listeTest = existingFolder(projectTests, desc.getClassName() + "ListeTest");
						runVelocity(desc, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
								"ListeTestContent.template", new File(listeTest, "content.txt"), TEMPLATE_PACKAGE, log);
						vaadinatorConfig.getCommonMap().put("prop", new String[] { "Edit", "Files", "Properties",
								"RecentChanges", "Refactor", "Search", "Test", "Versions", "WhereUsed" });
						runVelocity(desc, vaadinatorConfig.getCommonMap(), null, null, null, null, null,
								"Properties.template", new File(listeTest, "properties.xml"), TEMPLATE_PACKAGE, log);
						vaadinatorConfig.getCommonMap().remove("prop");
					}
				}
			}
		}

	}
}
