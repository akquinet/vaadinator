package org.vergien.vaadinator.codegenerator.webdriver;

import static de.akquinet.engineering.vaadinator.util.GeneratorUtil.*;
import de.akquinet.engineering.vaadinator.generator.CodeGenerator;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public class WebDriverCodeGenerator implements CodeGenerator {
	private static final String TEMPLATE_PACKAGE = "/org/vergien/vaadinator/templates/webdriver/";

	@Override
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws Exception {

		vaadinatorConfig.getLog().info("Generating WebDriver PageObjects");
		boolean generatePages = true;
		if ("false".equalsIgnoreCase(vaadinatorConfig.getGeneratorOptions().get("webDriverPages"))) {
			generatePages = false;
		}
		if (vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.SOURCES
				|| vaadinatorConfig.getGenTypeEn() == VaadinatorConfig.GenType.ALL) {
			if (vaadinatorConfig.isHasDisplayBeans()) {
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
							if (generatePages) {
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
}
