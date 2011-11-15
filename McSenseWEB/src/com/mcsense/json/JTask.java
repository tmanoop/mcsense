package com.mcsense.json;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class JTask {
	
	int taskId;
	String taskDescription;
	int providerPersonId;
	String taskStatus;
	String taskType;
	String taskName;
	int clientPay;
	int taskDuration;
	String sensedDataFileLocation;
	String accelerometer;
	String gps;
	String camera;
	String mic;
	String wifi;
	String bluetooth;
	String magnetometer;
	String proximitySensor;
	String ambientLightSensor;
	Timestamp taskAcceptedTime;
	Timestamp taskCompletionTime;
	Timestamp taskExpirationTime;
	Timestamp taskCreatedTime;
	
	public JTask(int id, String taskDesc){
		taskId = id;
		taskDescription = taskDesc;//replace with any new desc field from db
//		taskStatus = "Pending";
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
	
}
