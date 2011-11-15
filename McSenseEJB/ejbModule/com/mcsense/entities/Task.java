package com.mcsense.entities;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * The persistent class for the TASK database table.
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "Task.findByID", query = "Select t from Task t where t.taskId = :taskId "),
		@NamedQuery(name = "Task.findByIDAndProvider", query = "Select t from Task t where t.taskId = :taskId and t.providerPersonId = :providerId "),
		@NamedQuery(name = "Task.findByStatusAndId", query = "Select t from Task t where t.taskStatus = :status and t.providerPersonId = :providerId ORDER BY t.taskId DESC "),
		@NamedQuery(name = "Task.findByDesc", query = "Select t from Task t where t.taskName = :desc ORDER BY t.taskId DESC"),
		@NamedQuery(name = "Task.findByStatus", query = "Select t from Task t where t.taskStatus = :status and t.taskExpirationTime >= CURRENT_TIMESTAMP ORDER BY t.taskId DESC"),
		@NamedQuery(name = "Task.findCompleted", query = "Select t from Task t where t.taskStatus in :statuses and t.providerPersonId = :providerId ORDER BY t.taskId DESC "),
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
	
	@Column(name = "TASK_DESC")
	private String taskDesc;
	
	@Column(name = "CLIENT_PAY")
	private int clientPay;
	
	@Column(name = "TASK_DURATION")
	private int taskDuration;
	
	@Column(name = "SENSED_DATA_FILE_LOCATION")
	private String sensedDataFileLocation;
	
	@Column(name = "ACCELEROMETER")
	private String accelerometer;
	
	@Column(name = "GPS")
	private String gps;
	
	@Column(name = "CAMERA")
	private String camera;
	
	@Column(name = "MIC")
	private String mic;
	
	@Column(name = "WIFI")
	private String wifi;
	
	@Column(name = "BLUETOOTH")
	private String bluetooth;
	
	@Column(name = "MAGNETOMETER")
	private String magnetometer;
	
	@Column(name = "PROXIMITYSENSOR")
	private String proximitySensor;
	
	@Column(name = "AMBIENTLIGHTSENSOR")
	private String ambientLightSensor;
	
	@Column(name = "TASK_ACCEPTED_TIME")
	private Timestamp taskAcceptedTime;
	
	@Column(name = "TASK_COMPLETION_TIME")
	private Timestamp taskCompletionTime;
	
	@Column(name = "TASK_EXPIRATION_TIME")
	private Timestamp taskExpirationTime;
	
	@Column(name = "TASK_CREATED_TIME")
	private Timestamp taskCreatedTime;
	
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
	
	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public int getClientPay() {
		return clientPay;
	}

	public void setClientPay(int clientPay) {
		this.clientPay = clientPay;
	}

	public int getTaskDuration() {
		return taskDuration;
	}

	public void setTaskDuration(int taskDuration) {
		this.taskDuration = taskDuration;
	}

	public String getSensedDataFileLocation() {
		return sensedDataFileLocation;
	}

	public void setSensedDataFileLocation(String sensedDataFileLocation) {
		this.sensedDataFileLocation = sensedDataFileLocation;
	}

	public String getAccelerometer() {
		return accelerometer;
	}

	public void setAccelerometer(String accelerometer) {
		this.accelerometer = accelerometer;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(String bluetooth) {
		this.bluetooth = bluetooth;
	}

	public String getMagnetometer() {
		return magnetometer;
	}

	public void setMagnetometer(String magnetometer) {
		this.magnetometer = magnetometer;
	}

	public String getProximitySensor() {
		return proximitySensor;
	}

	public void setProximitySensor(String proximitySensor) {
		this.proximitySensor = proximitySensor;
	}

	public String getAmbientLightSensor() {
		return ambientLightSensor;
	}

	public void setAmbientLightSensor(String ambientLightSensor) {
		this.ambientLightSensor = ambientLightSensor;
	}

	public Timestamp getTaskAcceptedTime() {
		return taskAcceptedTime;
	}

	public void setTaskAcceptedTime(Timestamp taskAcceptedTime) {
		this.taskAcceptedTime = taskAcceptedTime;
	}

	public Timestamp getTaskCompletionTime() {
		return taskCompletionTime;
	}

	public void setTaskCompletionTime(Timestamp taskCompletionTime) {
		this.taskCompletionTime = taskCompletionTime;
	}

	public Timestamp getTaskExpirationTime() {
		return taskExpirationTime;
	}

	public void setTaskExpirationTime(Timestamp taskExpirationTime) {
		this.taskExpirationTime = taskExpirationTime;
	}

	public Timestamp getTaskCreatedTime() {
		return taskCreatedTime;
	}

	public void setTaskCreatedTime(Timestamp taskCreatedTime) {
		this.taskCreatedTime = taskCreatedTime;
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