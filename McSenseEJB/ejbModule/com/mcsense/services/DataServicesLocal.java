package com.mcsense.services;
import javax.ejb.Local;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

@Local
public interface DataServicesLocal {

	EntityManager getEM();

	<E extends Entity> E findEntity(Class<E> entityType, Object primaryKey);

	void test();

	<T> T merge(T entity);

	void persist(Object entity);

	void remove(Object entity);
}
