package org.vergien.vaadinator.codegenerator.webdriver;

import de.akquinet.engineering.vaadinator.generator.CodeGenerator;
import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public class WebDriverCodeGenerator implements CodeGenerator {

	@Override
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws Exception {

		vaadinatorConfig.getLog().info("Generating WebDriver PageObjects");
	}

}
