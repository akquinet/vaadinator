package de.akquinet.engineering.vaadinator.generator;

import de.akquinet.engineering.vaadinator.mojo.VaadinatorConfig;

public interface CodeGenerator {
	public void generateCode(VaadinatorConfig vaadinatorConfig) throws Exception;
}
