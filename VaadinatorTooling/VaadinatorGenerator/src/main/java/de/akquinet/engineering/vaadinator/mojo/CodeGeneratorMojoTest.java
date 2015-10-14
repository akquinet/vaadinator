package de.akquinet.engineering.vaadinator.mojo;

import static org.junit.Assert.assertThat;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class CodeGeneratorMojoTest {
	@Test
	public void testToValidJavaClassName() {
		CodeGeneratorMojo mojo = new CodeGeneratorMojo();

		assertThat(mojo.toValidJavaClassName("vaadinator-test"), is("VaadinatorTest"));
		assertThat(mojo.toValidJavaClassName("Vaadinator-Test"), is("VaadinatorTest"));
		assertThat(mojo.toValidJavaClassName("vaadinator-test_1"), is("VaadinatorTest1"));
	}
}
