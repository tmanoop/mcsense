package com.mcsense.services;
import java.util.List;

import javax.ejb.Local;

import com.mcsense.entities.Task;

@Local
public interface TaskServicesLocal {

	Task createTask(Task t);

	Task createTask(String id, String taskDesc);

	Task updateTask(String id, String task);

	List<Task> getTasks(String status, String id);

	Task createTask(String id, String taskDesc, String taskType);

}
