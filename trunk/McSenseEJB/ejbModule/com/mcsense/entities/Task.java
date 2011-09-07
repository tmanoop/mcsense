package com.mcsense.entities;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.persistence.*;

/**
 * The persistent class for the TASK database table.
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "Task.findByDesc", query = "Select t from Task t where t.taskName = :desc ORDER BY t.taskId DESC"),
		@NamedQuery(name = "Task.findByStatus", query = "Select t from Task t where t.taskStatus = :status ORDER BY t.taskId DESC"),
		@NamedQuery(name = "Task.findAll", query = "Select t from Task t ORDER BY t.taskId DESC"), })
public class Task implements Entity, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id")
	@SequenceGenerator(name = "task_id", sequenceName = "task_id", allocationSize = 1)
	@Column(name = "TASK_ID")
	private int taskId;

	@Column(name = "CLIENT_PERSON_ID", insertable = false, updatable = false)
	private int clientPersonId;

	@Column(name = "PARENT_TASK_ID")
	private int parentTaskId;

	@Column(name = "PROVIDER_PERSON_ID")
	private int providerPersonId;

	@Column(name = "TASK_STATUS")
	private String taskStatus;

	@Column(name = "TASK_TYPE")
	private String taskType;

	@Column(name = "TASK_NAME")
	private String taskName;
	
	// bi-directional many-to-one association to People
	@ManyToOne
	@JoinColumn(name = "CLIENT_PERSON_ID", referencedColumnName = "PERSON_ID")
	private People people;

	public Task() {
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
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

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public People getPeople() {
		return this.people;
	}

	public void setPeople(People people) {
		this.people = people;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getClientPersonId() {
		return clientPersonId;
	}

	public void setClientPersonId(int clientPersonId) {
		this.clientPersonId = clientPersonId;
	}

}