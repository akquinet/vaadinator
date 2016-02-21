package de.akquinet.engineering.vaadinator.mojo;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.akquinet.engineering.vaadinator.generator.CodeGenerator;

import static org.hamcrest.CoreMatchers.*;

public class CodeGeneratorMojoTest {
	@Test
	public void testToValidJavaClassName() {
		CodeGeneratorMojo mojo = new CodeGeneratorMojo();

		// convert characters (esp. first)
		assertThat(mojo.toValidJavaClassName("vaadinator-test"), is("VaadinatorTest"));
		assertThat(mojo.toValidJavaClassName("Vaadinator-Test"), is("VaadinatorTest"));
		assertThat(mojo.toValidJavaClassName("vaadinator-test_1"), is("VaadinatorTest1"));

		// keep existing casing
		assertThat(mojo.toValidJavaClassName("vaadinatorExample-test_1"), is("VaadinatorExampleTest1"));
		assertThat(mojo.toValidJavaClassName("VaadinatorExample-test_1"), is("VaadinatorExampleTest1"));
		assertThat(mojo.toValidJavaClassName("Vaadinatorexample-test_1"), is("VaadinatorexampleTest1"));
	}

	@Test
	@Ignore
	public void testInitGenerators() {
		CodeGeneratorMojo mojo = new CodeGeneratorMojo();

		List<CodeGenerator> codeGenerators = mojo.initGenerators();

		assertThat("There should be the default code generator and the test edit one", codeGenerators.size(), is(2));
	}
}
