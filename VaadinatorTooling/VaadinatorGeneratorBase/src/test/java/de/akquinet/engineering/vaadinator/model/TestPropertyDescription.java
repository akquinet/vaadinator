package de.akquinet.engineering.vaadinator.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

public class TestPropertyDescription {

	private PropertyDescription propertyDescriptionUnderTest;

	@Before
	public void setUp() {
		propertyDescriptionUnderTest = new PropertyDescription();
	}

	@Test
	public void testGetPropertyClassTypeParameter() {
		propertyDescriptionUnderTest.setPropertyClassName("List<String>");
		assertThat(propertyDescriptionUnderTest.getPropertyClassTypeParameter(), is("String"));
	}

}
