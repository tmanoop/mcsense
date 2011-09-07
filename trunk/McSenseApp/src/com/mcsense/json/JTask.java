package com.mcsense.json;

import android.os.Parcel;
import android.os.Parcelable;

public class JTask implements Parcelable {
	
	int taskId;
	String taskDescription;
	int providerPersonId;
	String taskStatus;
	String taskType;
	String taskName;
	
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
	}
	
	private void readFromParcel(Parcel in) {

		taskId= in.readInt();
		taskDescription= in.readString();
		providerPersonId= in.readInt();
		taskStatus= in.readString();
		taskType= in.readString();
		taskName= in.readString();
	}

}
