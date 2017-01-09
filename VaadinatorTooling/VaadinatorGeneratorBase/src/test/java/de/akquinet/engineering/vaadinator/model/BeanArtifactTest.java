package de.akquinet.engineering.vaadinator.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeanArtifactTest {

	@Test
	public void testIsView() {
		assertTrue(BeanArtifact.ADD_VIEW.isView());
		assertTrue(BeanArtifact.ADD_VIEW_IMPL.isView());
		assertFalse(BeanArtifact.DAO.isView());
		assertFalse(BeanArtifact.DAO_IMPL.isView());
	}

	@Test
	public void testIsDao() {
		assertFalse(BeanArtifact.ADD_VIEW.isDao());
		assertFalse(BeanArtifact.ADD_VIEW_IMPL.isDao());
		assertTrue(BeanArtifact.DAO.isDao());
		assertTrue(BeanArtifact.DAO_IMPL.isDao());
	}

	@Test
	public void testIsPresenter() {
		assertFalse(BeanArtifact.ADD_VIEW.isPresenter());
		assertFalse(BeanArtifact.ADD_VIEW_IMPL.isPresenter());
		assertFalse(BeanArtifact.DAO.isPresenter());
		assertFalse(BeanArtifact.DAO_IMPL.isPresenter());
		assertTrue(BeanArtifact.ADD_PRES.isPresenter());
		assertTrue(BeanArtifact.LIST_PRES_IMPL.isPresenter());
	}

	@Test
	public void testPerProfileValues() {
		BeanArtifact[] perProfileValues = BeanArtifact.perProfileValues();
		assertThat(perProfileValues.length, is(20));
	}
	
	@Test
	public void testPerBeanValues() {
		BeanArtifact[] perProfileValues = BeanArtifact.perBeanValues();
		assertThat(perProfileValues.length, is(2));
	}

}
