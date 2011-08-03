package com.mcsense.services;

import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.mcsense.entities.People;
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
}
