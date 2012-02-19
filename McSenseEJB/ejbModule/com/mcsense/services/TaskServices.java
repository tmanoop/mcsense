package com.mcsense.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

	private static final int FIVE = 5;
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
	public Task updateTask(Task t) {

		try {
						
			t = dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task failed to merge.");
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
				} else if(status.equals("V")) {
					System.out.println("Status:"+status);
					Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByStatus").setParameter("status", status);				
					tList = (List<Task>) q.getResultList();
				}else{
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
	public List<Task> getAllTasksbyStatus(String status) {
		List<Task> tList = new ArrayList<Task>();
		
		try {			
			if(status != null && !status.equals("") && !status.equals("ALL")){
				System.out.println("Status:"+status);
				Query q = dataServicesLocal.getEM().createNamedQuery("Task.findAllByStatus").setParameter("status", status);				
				tList = (List<Task>) q.getResultList();
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
	public List<Task> getTasksbyStatus(String status, String providerId) {
		List<Task> tList = new ArrayList<Task>();
		
		try {
			if(status.equals("IP")){
				Timestamp endDate = getTonightTimestamp();
				Timestamp now = getTonightTimestamp();
				now.setDate(now.getDate()-1);
				Timestamp startDate = now;
				System.out.println("Status:"+status+" providerId:"+providerId+" start: "+now+" end: "+endDate);
				Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByStatusAndId").setParameter("status", status).setParameter("providerId", new Integer(providerId)).setParameter("startDate", startDate).setParameter("endDate", endDate);				
				tList = (List<Task>) q.getResultList();
			} else if(status.equals("P")) {
				//check for participants only. return available tasks only for them.
				Query qPerson = dataServicesLocal.getEM().createNamedQuery("People.findByPrimaryKey").setParameter("id", new Integer(providerId));
				People p = (People)qPerson.getSingleResult();
				if(p!=null && !p.getNjitDepartment().equals("")){
					System.out.println("Status:"+status);
					Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByStatus").setParameter("status", status);				
					tList = (List<Task>) q.getResultList();
				}
			} else if(status.equals("C")) {
				System.out.println("Status:"+status);
				List<String> statuses = Arrays.asList("C", "E", "V");
				Query q = dataServicesLocal.getEM().createNamedQuery("Task.findCompleted").setParameter("statuses", statuses).setParameter("providerId", new Integer(providerId));				
				tList = (List<Task>) q.getResultList();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	public Task getTaskByIdAndProvider(String providerId, String taskId){
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByIDAndProvider").setParameter("taskId", new Integer(taskId)).setParameter("providerId", new Integer(providerId));
			
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
			t.setTaskAcceptedTime(McUtility.getTimestamp());
			t.setTaskStatus("IP");	//IP - in progress
			
			dataServicesLocal.merge(t);
			dataServicesLocal.getEM().flush();
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
	}

	@Override
	public void acceptParentTask(String providerId, String taskId, String taskName) {
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByID").setParameter("taskId", new Integer(taskId));
			
			t = (Task) q.getSingleResult();		
			t.setProviderPersonId(new Integer(providerId));
			t.setTaskAcceptedTime(McUtility.getTimestamp());
			t.setTaskName(taskName);
			t.setTaskStatus("IP");	//IP - in progress
			
			dataServicesLocal.merge(t);
			dataServicesLocal.getEM().flush();
			
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
			t.setTaskCompletionTime(McUtility.getTimestamp());
			t.setTaskStatus("C");	//IP - in progress
			
			dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
	}

	@Override
	public void completeTask(String providerId, String taskId,
			String completionStatus) {
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByID").setParameter("taskId", new Integer(taskId));
			
			t = (Task) q.getSingleResult();		
			t.setProviderPersonId(new Integer(providerId));
			t.setTaskCompletionTime(McUtility.getTimestamp());
			t.setTaskStatus(completionStatus);	//IP - in progress
			if(completionStatus.equals("E"))
				t.setTaskDesc(t.getTaskDesc()+"\nTask Failure Reason: *** Task failed for not recording at least 6 hours of sensing data. ***");	//Set failed reason
			
			dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
	}

	@Override
	public List<Task> getChildTasks(String parentTaskId) {
		List<Task> tList = new ArrayList<Task>();
		
		try {
		
			System.out.println("parentTaskId: "+parentTaskId);
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByParentId").setParameter("status", "P").setParameter("parentTaskId", new Integer(parentTaskId));				
			tList = (List<Task>) q.getResultList();
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tList;
	}

	@Override
	public boolean hasPendingTask(String providerId, String taskType) {
		List<Task> tList = new ArrayList<Task>();
		
		try {
		
			System.out.println("providerId: "+providerId);
			List<String> statuses = Arrays.asList("IP");
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByTaskTypeAndId").setParameter("statuses", statuses).setParameter("taskType", taskType).setParameter("providerId", new Integer(providerId));				
			tList = (List<Task>) q.getResultList();
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tList.size()>0)
			return true;
		else
			return false;
	}

	@Override
	public boolean hasReachedPhotoLimit(String providerId) {
		List<Task> tList = new ArrayList<Task>();
		
		try {
		
			System.out.println("providerId: "+providerId);
			List<String> statuses = Arrays.asList("C", "V");
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByTaskTypeAndIdPerDay").setParameter("statuses", statuses).setParameter("taskType", "photo").setParameter("providerId", new Integer(providerId));				
			tList = (List<Task>) q.getResultList();
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tList.size()>=FIVE)
			return true;
		else
			return false;
	}
	
	private Timestamp getTonightTimestamp() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		now.setHours(23);
		now.setMinutes(0);
		now.setNanos(0);
		now.setSeconds(0);
		return now;
	}

	/**
	 * Just for quick changes, SensedDataFileLocation is used to store 2 different values.		
	 * one for sensed duration in case of sensing task.		
	 * second for photo location in case of photo task.		
	 * Later change this to have new field for each. or remember to maintain appropriately.
	 */
	@Override
	public void completeTask(String providerId, String taskId,
			String completionStatus, String currentLocation) {
		Task t = null;
		try {
			
			Query q = dataServicesLocal.getEM().createNamedQuery("Task.findByID").setParameter("taskId", new Integer(taskId));
			
			t = (Task) q.getSingleResult();		
			t.setProviderPersonId(new Integer(providerId));
			t.setTaskCompletionTime(McUtility.getTimestamp());
			t.setTaskStatus(completionStatus);	//IP - in progress
			t.setSensedDataFileLocation(currentLocation);
			if(completionStatus.equals("E")){
				if(t.getTaskType().equals("photo"))
					t.setTaskDesc(t.getTaskDesc()+"\nTask Failure Reason: *** Photo not uploaded before task expiration time. ***");	//Set failed reason
				else
					t.setTaskDesc(t.getTaskDesc()+"\nTask Failure Reason: *** Task failed for not recording at least 6 hours of sensing data. ***");	//Set failed reason
			}
			dataServicesLocal.merge(t);
			
		} catch (Exception e) {
			System.out.println("Task not found.");
			e.printStackTrace();
		}
	}
}
