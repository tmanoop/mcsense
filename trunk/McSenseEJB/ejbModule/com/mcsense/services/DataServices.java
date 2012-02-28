package com.mcsense.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mcsense.entities.People;

/**
 * Session Bean implementation class DataServices
 */
@Stateless
public class DataServices implements DataServicesLocal {

	@PersistenceContext(name = "McSenseEJB", unitName = "McSenseEJB")
	private EntityManager emTransaction;

	/**
	 * Default constructor.
	 */
	public DataServices() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public EntityManager getEM() {
		// TODO Auto-generated method stub
		return emTransaction;
	}

	@Override
	public <E extends Entity> E findEntity(Class<E> entityType,
			Object primaryKey) {
		return emTransaction.find(entityType, primaryKey);
	}

	@Override
	public <T> T merge(T entity) {
		return getEM().merge(entity);
	}

	@Override
	public void persist(Object entity) {
		getEM().persist(entity);
	}

	@Override
	public void remove(Object entity) {
		getEM().remove(getEM().merge(entity));
		//getEM().refresh(entity);
	}

	@Override
	public void test() {
		try {
			Query q = emTransaction.createQuery("select e from People e");
			People p = (People) q.getSingleResult();
			//System.out.println(p.getPersonFname());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
