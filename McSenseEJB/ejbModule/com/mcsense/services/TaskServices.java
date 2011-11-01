package com.mcsense.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.mcsense.entities.People;
import com.mcsense.entities.Task;
import com.mcsense.util.McUtility;

/**
 * Session Bean implementation class TaskServices
 */
@Stateless
public class TaskServices implements TaskServicesLocal {

	@EJB(name="com.mcsense.services.DataServices")
	DataServicesLocal dataServicesLocal;
	@EJB(name="com.mcsense.services.BankAdminServices")
	BankAdminServicesLocal bankAdminServicesLocal;
    /**
     * Default constructor. 
     */
    public TaskServices() {
    	bankAdminServicesLocal = McUtility.lookupEJB("java:global/McSense/McSenseEJB/BankAdminServices!com.mcsense.services.BankAdminServicesLocal");
    }

	@Override
	public Task createTask(Task t) {
		t.setTaskStatus("P");
		String id = t.getClientPersonId()+"";
		People p = (People)bankAdminServicesLocal.findPersonByID(id);
		t.setPeople(p);
		
		dataServicesLocal.persist(t);
		return t;
	}
	
	@Override
	public Task createTask(String id, String taskName){
		Task t = new Task();
		t.setClientPersonId(new Integer(id));
		t.setTaskName(taskName);
//		
//		People p = (People)bankAdminServicesLocal.findPersonByID(id);
//		t.setPeople(p);
		t = createTask(t);
		return t;
	}
	
	@Override
	public Task createTask(String id, String taskName, String taskType){
		Task t = new Task();
		t.setClientPersonId(new Integer(id));
		t.setTaskName(taskName);
		t.setTaskType(taskType);
//		
//		People p = (People)bankAdminServicesLocal.findPersonByID(id);
//		t.setPeople(p);
		t = createTask(t);
		return t;
	}

	@Override
	public Task updateTask(String id, String taskDesc) {
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByDesc").setParameter("desc", taskDesc);
			
			List<Task> tList = (List<Task>) q.getResultList();
			t = tList.get(0);			
			t.setProviderPersonId(new Integer(id));
			t.setTaskStatus("IP");	//IP - in progress
			
			dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
		
		return t;
	}
	
	@Override
	public List<Task> getTasks(String status, String providerId) {
		List<Task> tList = new ArrayList<Task>();
		
		try {			
			if(status != null && !status.equals("") && !status.equals("ALL")){
				if(providerId != null && !providerId.equals("") && !status.equals("P")){
					System.out.println("Status:"+status+" providerId:"+providerId);
					Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByStatusAndId").setParameter("status", status).setParameter("providerId", new Integer(providerId));				
					tList = (List<Task>) q.getResultList();
				} else{
					System.out.println("Status:"+status);
					Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByStatus").setParameter("status", status);				
					tList = (List<Task>) q.getResultList();
				}
			} else {				
				Query q = dataServicesLocal.getEM().createNamedQuery("Task.findAll");
				tList = (List<Task>) q.getResultList();
			}			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
		
		return tList;
	}

	@Override
	public Task getTaskById(String taskId){
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByID").setParameter("taskId", new Integer(taskId));
			
			t = (Task) q.getSingleResult();	
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
		return t;
	}
	
	@Override
	public void acceptTask(String providerId, String taskId) {
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByID").setParameter("taskId", new Integer(taskId));
			
			t = (Task) q.getSingleResult();		
			t.setProviderPersonId(new Integer(providerId));
			t.setTaskStatus("IP");	//IP - in progress
			
			dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
	}

	@Override
	public void completeTask(String providerId, String taskId) {
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByID").setParameter("taskId", new Integer(taskId));
			
			t = (Task) q.getSingleResult();		
			t.setProviderPersonId(new Integer(providerId));
			t.setTaskStatus("C");	//IP - in progress
			
			dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
	}
}
