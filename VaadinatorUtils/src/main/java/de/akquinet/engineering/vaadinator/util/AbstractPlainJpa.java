/*
 * Copyright 2014 akquinet engineering GmbH
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
package de.akquinet.engineering.vaadinator.util;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

/**
 * 
 * @author olli
 *
 */
public abstract class AbstractPlainJpa {

	public AbstractPlainJpa() {
		super();
	}

	public AbstractPlainJpa(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	private EntityManagerFactory entityManagerFactory;

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public interface Callback {

		public Object perform();

	}

	// see service test case (mock Entity mgr)
	protected Object requireJpaTransaction(Callback callback, Map<String, Object> context) {
		EntityTransaction transaction = null;
		EntityManager entityManager = null;
		boolean newTransaction = entityManagers.get() == null;
		if (newTransaction) {
			entityManager = entityManagerFactory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManagers.set(entityManager);
		}
		boolean success = false;
		try {
			Object res = callback.perform();
			if (newTransaction) {
				transaction.commit();
			}
			success = true;
			return res;
		} finally {
			if (newTransaction) {
				entityManagers.remove();
				try {
					if ((!success) && transaction.isActive()) {
						transaction.rollback();
					}
				} finally {
					entityManager.close();
				}
			}
		}
	}

	private static final ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>();

	protected final EntityManager getEntityManager() {
		return entityManager;
	}

	private static final EntityManager entityManager = new EntityManager() {

		@Override
		public <T> T unwrap(Class<T> cls) {
			return entityManagers.get().unwrap(cls);
		}

		@Override
		public void setProperty(String propertyName, Object value) {
			entityManagers.get().setProperty(propertyName, value);
		}

		@Override
		public void setFlushMode(FlushModeType flushMode) {
			entityManagers.get().setFlushMode(flushMode);
		}

		@Override
		public void remove(Object entity) {
			entityManagers.get().remove(entity);
		}

		@Override
		public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
			entityManagers.get().refresh(entity, lockMode, properties);
		}

		@Override
		public void refresh(Object entity, LockModeType lockMode) {
			entityManagers.get().refresh(entity, lockMode);
		}

		@Override
		public void refresh(Object entity, Map<String, Object> properties) {
			entityManagers.get().refresh(entity, properties);
		}

		@Override
		public void refresh(Object entity) {
			entityManagers.get().refresh(entity);
		}

		@Override
		public void persist(Object entity) {
			entityManagers.get().persist(entity);
		}

		@Override
		public <T> T merge(T entity) {
			return entityManagers.get().merge(entity);
		}

		@Override
		public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
			entityManagers.get().lock(entity, lockMode, properties);
		}

		@Override
		public void lock(Object entity, LockModeType lockMode) {
			entityManagers.get().lock(entity, lockMode);
		}

		@Override
		public void joinTransaction() {
			entityManagers.get().joinTransaction();
		}

		@Override
		public boolean isOpen() {
			return entityManagers.get().isOpen();
		}

		@Override
		public EntityTransaction getTransaction() {
			return entityManagers.get().getTransaction();
		}

		@Override
		public <T> T getReference(Class<T> entityClass, Object primaryKey) {
			return entityManagers.get().getReference(entityClass, primaryKey);
		}

		@Override
		public Map<String, Object> getProperties() {
			return entityManagers.get().getProperties();
		}

		@Override
		public Metamodel getMetamodel() {
			return entityManagers.get().getMetamodel();
		}

		@Override
		public LockModeType getLockMode(Object entity) {
			return entityManagers.get().getLockMode(entity);
		}

		@Override
		public FlushModeType getFlushMode() {
			return entityManagers.get().getFlushMode();
		}

		@Override
		public EntityManagerFactory getEntityManagerFactory() {
			return entityManagers.get().getEntityManagerFactory();
		}

		@Override
		public Object getDelegate() {
			return entityManagers.get().getDelegate();
		}

		@Override
		public CriteriaBuilder getCriteriaBuilder() {
			return entityManagers.get().getCriteriaBuilder();
		}

		@Override
		public void flush() {
			entityManagers.get().flush();
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
			return entityManagers.get().find(entityClass, primaryKey, lockMode, properties);
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
			return entityManagers.get().find(entityClass, primaryKey, lockMode);
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
			return entityManagers.get().find(entityClass, primaryKey, properties);
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey) {
			return entityManagers.get().find(entityClass, primaryKey);
		}

		@Override
		public void detach(Object entity) {
			entityManagers.get().detach(entity);
		}

		@Override
		public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
			return entityManagers.get().createQuery(qlString, resultClass);
		}

		@Override
		public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
			return entityManagers.get().createQuery(criteriaQuery);
		}

		@Override
		public Query createQuery(String qlString) {
			return entityManagers.get().createQuery(qlString);
		}

		@Override
		public Query createNativeQuery(String sqlString, String resultSetMapping) {
			return entityManagers.get().createNativeQuery(sqlString, resultSetMapping);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Query createNativeQuery(String sqlString, Class resultClass) {
			return entityManagers.get().createNativeQuery(sqlString, resultClass);
		}

		@Override
		public Query createNativeQuery(String sqlString) {
			return entityManagers.get().createNativeQuery(sqlString);
		}

		@Override
		public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
			return entityManagers.get().createNamedQuery(name, resultClass);
		}

		@Override
		public Query createNamedQuery(String name) {
			return entityManagers.get().createNamedQuery(name);
		}

		@Override
		public boolean contains(Object entity) {
			return entityManagers.get().contains(entity);
		}

		@Override
		public void close() {
			entityManagers.get().close();
		}

		@Override
		public void clear() {
			entityManagers.get().clear();
		}
	};

}
