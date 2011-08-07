package com.mcsense.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.mcsense.entities.Task;

/**
 * Session Bean implementation class TaskServices
 */
@Stateless
public class TaskServices implements TaskServicesLocal {

	@EJB(name="com.mcsense.services.DataServices")
	DataServicesLocal dataServicesLocal;
    /**
     * Default constructor. 
     */
    public TaskServices() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public Task createTask(Task t) {
		t.setTaskStatus("P");
		dataServicesLocal.persist(t);
		return t;
	}

}
