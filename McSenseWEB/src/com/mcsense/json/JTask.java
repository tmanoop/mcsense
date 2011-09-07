package com.mcsense.json;

public class JTask {
	
	int taskId;
	String taskDescription;
	int providerPersonId;
	String taskStatus;
	String taskType;
	String taskName;
	
	public JTask(int id, String taskName){
		taskId = id;
		taskDescription = taskName;//replace with any new desc field from db
		taskStatus = "Pending";
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public int getProviderPersonId() {
		return providerPersonId;
	}
	public void setProviderPersonId(int providerPersonId) {
		this.providerPersonId = providerPersonId;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
