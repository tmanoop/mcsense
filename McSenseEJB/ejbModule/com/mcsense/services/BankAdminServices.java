package com.mcsense.services;

import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.mcsense.entities.People;
import com.mcsense.entities.Reputation;
import com.mcsense.util.McUtility;

/**
 * Session Bean implementation class BankAdminServices
 */
@Stateless
public class BankAdminServices implements BankAdminServicesLocal {

	@EJB(name="com.mcsense.services.DataServices")
	DataServicesLocal dataServicesLocal;
    /**
     * Default constructor. 
     */
    public BankAdminServices() {
    	dataServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/DataServices!com.mcsense.services.DataServicesLocal");
    }

    @Override
    public List<People> findPersonByName(String name){
    	List<People> pList = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("People.findByLName").setParameter("name", name);
			
			pList = (List<People>) q.getResultList();
						
		} catch (Exception e) {
			System.out.println("PersonID not found.");
			e.printStackTrace();
		}
		
		return pList;
    }
    
    @Override
    public People findPersonByID(String ID){
    	People p = null;
		try {

			p = (People)dataServicesLocal.findEntity(People.class, new Integer(ID));
			
			//dataServicesLocal.test();
						
		} catch (Exception e) {
			System.out.println("PersonID not found.");
			e.printStackTrace();
		}
		
		if (p==null) {
			System.out.println("PersonID not found.");
		}
		return p;
    }
    
    @Override
    public int register(People p){
//    	Reputation r = new Reputation();
//    	r.setReputationScore(5);
//    	p.setReputation(r);
//    	r.setPeople(p);
    	try {
			dataServicesLocal.persist(p);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	List<People> newPerson = findPersonByName(p.getPersonLname());
    	
    	People tempPerson = newPerson.get(0);
    	try {
			Reputation r = new Reputation();
			r.setReputationScore(5);
			r.setPersonId(tempPerson.getPersonId());
			r.setPeople(tempPerson);
			tempPerson.setReputation(r);
			dataServicesLocal.persist(r);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return newPerson.get(0).getPersonId();
    	
    }
    
    @Override
    public boolean deleteReputation(String id) {
    	People p = findPersonByID(id);
    	try {
			dataServicesLocal.remove(p.getReputation());
//			dataServicesLocal.getEM().refresh(p);
//			dataServicesLocal.remove(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    public boolean deletePerson(String id){
    	People p = findPersonByID(id);
    	try {
//			dataServicesLocal.remove(p.getReputation());
//			dataServicesLocal.getEM().refresh(p);
			dataServicesLocal.remove(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    }
}
