package com.mcsense.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.mcsense.entities.People;
import com.mcsense.entities.Reputation;
import com.mcsense.security.SimpleCrypto;
import com.mcsense.util.McUtility;
import com.mcsense.util.ServiceConstants;

/**
 * Session Bean implementation class LoginServices
 */
@Stateless
public class LoginServices implements LoginServicesLocal {

	@EJB(name="com.mcsense.services.DataServices")
	DataServicesLocal dataServicesLocal;
    /**
     * Default constructor. 
     */
    public LoginServices() {
    	dataServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/DataServices!com.mcsense.services.DataServicesLocal");
    }

    @Override
    public People loginCheck(String emailId, String password){
    	
    	People p = null;
		try {

			Query q = dataServicesLocal.getEM().createNamedQuery("People.loginCheck").setParameter("emailId", emailId).setParameter("password", password);
			
			p = (People)q.getSingleResult();
						
		} catch (Exception e) {
			System.out.println("PersonID not found.");
//			e.printStackTrace();
		}
		
		if (p==null) {
			System.out.println("PersonID not found.");
		}
		return p;
    }
    
    @Override
    public People register(People p){
//    	Reputation r = new Reputation();
//    	r.setReputationScore(5);
//    	p.setReputation(r);
//    	r.setPeople(p);
    	String encryptedText = "";
    	try {
			encryptedText = SimpleCrypto.encrypt(ServiceConstants.SEED,p.getPassword());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println("encryptedText:"+encryptedText);
    	try {
			dataServicesLocal.persist(p);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	People newPerson = loginCheck(p.getEmailId(), encryptedText);
    	
    	try {
			Reputation r = new Reputation();
			r.setReputationScore(5);
			r.setPersonId(newPerson.getPersonId());
			r.setPeople(newPerson);
			newPerson.setReputation(r);
			dataServicesLocal.persist(r);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return newPerson;
    	
    }
}
