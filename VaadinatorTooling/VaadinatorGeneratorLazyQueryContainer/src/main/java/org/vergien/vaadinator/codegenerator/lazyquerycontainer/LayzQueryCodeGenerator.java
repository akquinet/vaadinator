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
package org.vergien.vaadinator.codegenerator.lazyquerycontainer;

import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.packageToFile;
import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.runVelocity;

import org.apache.maven.plugin.logging.Log;

import de.akquinet.engineering.vaadinator.generator.CodeGenerator;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public class LayzQueryCodeGenerator implements CodeGenerator {
	private static final String TEMPLATE_PACKAGE = "/org/vergien/vaadinator/templates/lazyquerycontainer/";
	private static final String DEFAULT_TEMPLATE_PACKAGE = "/de/akquinet/engineering/vaadinator/templates/";

	@Override
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws Exception {
		Log log = vaadinatorConfig.getLog();
		log.info("Generating lazy query containers");
		if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.SOURCES
				|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
			if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.SOURCES
					|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
				for (BeanDescription desc : vaadinatorConfig.getBeanDescriptions()) {
					if (desc.isDisplayed()) {
						for (DisplayProfileDescription p : desc.getDisplayProfiles()) {
							String componentPckg = desc.getViewPckg(p) + ".container";
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"LazyQueryContainer.template",
									packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), componentPckg,
											desc.getClassName(), "LazyQueryContainer.java"),
									TEMPLATE_PACKAGE, log);
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"LazyQuery.template", packageToFile(vaadinatorConfig.getTargetFolderSrcStart(),
											componentPckg, desc.getClassName(), "LazyQuery.java"),
									TEMPLATE_PACKAGE, log);
							runVelocity(desc, vaadinatorConfig.getCommonMap(), componentPckg, desc.getPckg(),
									desc.getPresenterPckg(p), desc.getViewPckg(p), p.getProfileName(),
									"LazyQueryFactory.template",
									packageToFile(vaadinatorConfig.getTargetFolderSrcStart(), componentPckg,
											desc.getClassName(), "LazyQueryFactory.java"),
									TEMPLATE_PACKAGE, log);
						}

					}
				}
			}
		}
	}

}
