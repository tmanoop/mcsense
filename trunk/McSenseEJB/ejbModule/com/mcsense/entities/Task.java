package com.mcsense.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the TASK database table.
 * 
 */
@Entity
@Table(name="TASK",schema="APP")
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TASK_ID")
	private int taskId;

	@Column(name="CLIENT_PERSON_ID")
	private int clientPersonId;

	@Column(name="PARENT_TASK_ID")
	private int parentTaskId;

	@Column(name="PROVIDER_PERSON_ID")
	private int providerPersonId;

	@Column(name="TASK_STATUS")
	private String taskStatus;

	@Column(name="TASK_TYPE")
	private String taskType;

	//bi-directional many-to-one association to Bank
	@OneToMany(mappedBy="task")
	private Set<Bank> banks;

    public Task() {
    }

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getClientPersonId() {
		return this.clientPersonId;
	}

	public void setClientPersonId(int clientPersonId) {
		this.clientPersonId = clientPersonId;
	}

	public int getParentTaskId() {
		return this.parentTaskId;
	}

	public void setParentTaskId(int parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public int getProviderPersonId() {
		return this.providerPersonId;
	}

	public void setProviderPersonId(int providerPersonId) {
		this.providerPersonId = providerPersonId;
	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Set<Bank> getBanks() {
		return this.banks;
	}

	public void setBanks(Set<Bank> banks) {
		this.banks = banks;
	}
	
}