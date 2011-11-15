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

	void acceptTask(String providerId, String taskId);

	void completeTask(String providerId, String taskId);

	Task getTaskById(String taskId);

	void completeTask(String providerId, String taskId, String completionStatus);

	List<Task> getTasksbyStatus(String status, String providerId);

	Task getTaskByIdAndProvider(String providerId, String taskId);

}
