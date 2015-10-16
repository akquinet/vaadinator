package de.akquinet.engineering.vaadinator.mojo;

import static org.junit.Assert.assertThat;

import org.junit.Test;
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
}
