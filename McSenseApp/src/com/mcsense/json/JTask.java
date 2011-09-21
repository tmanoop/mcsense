package com.mcsense.json;

import java.math.BigDecimal;

import android.os.Parcel;
import android.os.Parcelable;

public class JTask implements Parcelable {
	
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
	
	public JTask(Parcel in){
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public JTask createFromParcel(Parcel in) {
            return new JTask (in);
        }

        public JTask [] newArray(int size) {
            return new JTask [size];
        }
    };
	
	public JTask(int id, String desc){
		taskId = id;
		taskDescription = desc;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(taskId);
        dest.writeString(taskDescription);
        dest.writeInt(providerPersonId);
        dest.writeString(taskStatus);
        dest.writeString(taskType);
        dest.writeString(taskName);
        dest.writeInt(clientPay);
    	dest.writeInt(taskDuration);
    	dest.writeString(sensedDataFileLocation);
    	dest.writeString(accelerometer);
    	dest.writeString(gps);
    	dest.writeString(camera);
    	dest.writeString(mic);
    	dest.writeString(wifi);
    	dest.writeString(bluetooth);
    	dest.writeString(magnetometer);
    	dest.writeString(proximitySensor);
    	dest.writeString(ambientLightSensor);
	}
	
	private void readFromParcel(Parcel in) {

		taskId= in.readInt();
		taskDescription= in.readString();
		providerPersonId= in.readInt();
		taskStatus= in.readString();
		taskType= in.readString();
		taskName= in.readString();
		clientPay=in.readInt();
		taskDuration= in.readInt();
		sensedDataFileLocation= in.readString();
		accelerometer= in.readString();
		gps= in.readString();
		camera= in.readString();
		mic= in.readString();
		wifi= in.readString();
		bluetooth= in.readString();
		magnetometer= in.readString();
		proximitySensor= in.readString();
		ambientLightSensor= in.readString();
	}

}
