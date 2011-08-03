package com.mcsense.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.spi.PersistenceUnitTransactionType;

import com.mcsense.entities.People;

public class DataService {

	private static final Object TRANSACTION_TYPE = "transaction-type";
	private static final Object JDBC_DRIVER = "javax.persistence.jdbc.driver";
	private static final Object JDBC_URL = "javax.persistence.jdbc.url";
	private static final Object JDBC_USER = "javax.persistence.jdbc.user";
	private static final Object JDBC_PASSWORD = "javax.persistence.jdbc.password";
	private static final Object LOGGING_LEVEL = null;
	private static final Object LOGGING_TIMESTAMP = null;
	private static final Object LOGGING_THREAD = null;
	private static final Object LOGGING_SESSION = null;
	private static final Object TARGET_SERVER = null;

	@PersistenceContext(name="McSenseDB",unitName="McSenseDB",type=PersistenceContextType.TRANSACTION)
    private static EntityManager emTransaction;
	
	public static void main(String[] args) {
		People p = getEntity("1");
		System.out.println("PersonID: "+p.getPersonId());
	}

	private static EntityManager getEntityManager() {
		
		Map properties = new HashMap();
		 
	    // Ensure RESOURCE_LOCAL transactions is used.
	    properties.put(TRANSACTION_TYPE,
	        PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
	 
	    // Configure the internal EclipseLink connection pool
	    properties.put(JDBC_DRIVER, "org.apache.derby.jdbc.ClientDriver");
	    properties.put(JDBC_URL, "jdbc:derby://localhost:1527/McSenseDB;create=true;");
	    properties.put(JDBC_USER, "app");
	    properties.put(JDBC_PASSWORD, "app");
	 
	    // Configure logging. FINE ensures all SQL is shown
//	    properties.put(LOGGING_LEVEL, "FINE");
//	    properties.put(LOGGING_TIMESTAMP, "false");
//	    properties.put(LOGGING_THREAD, "false");
//	    properties.put(LOGGING_SESSION, "false");
	 
	    // Ensure that no server-platform is configured
//	    properties.put(TARGET_SERVER, TargetServer.None);
		
//		EntityManagerFactory emf = Persistence
//				.createEntityManagerFactory("McSenseDB");

	    EntityManagerFactory emf = Persistence.createEntityManagerFactory("McSenseDB", properties);
	    
		EntityManager em = emf.createEntityManager();
		
		return em;
	}

	public static People getEntity(String ID){
		return getEmTransaction().find(People.class, new Integer(ID));
	}
	
	public <E extends Entity> E findEntity(Class<E> entityType , Object primaryKey){
		return  getEntityManager().find(entityType , primaryKey);
	}

	public void setEmTransaction(EntityManager emTransaction) {
		this.emTransaction = emTransaction;
	}

	public static EntityManager getEmTransaction() {
		return emTransaction;
	}
}
