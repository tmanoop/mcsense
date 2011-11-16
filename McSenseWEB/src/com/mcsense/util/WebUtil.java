package com.mcsense.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mcsense.entities.Task;
import com.mcsense.json.JTask;

public class WebUtil {
	
	public static final Object adminID = "mt57@njit.edu";

	public static JTask mapToJsonTask(Task t) {
		JTask jTask = new JTask(t.getTaskId(),t.getTaskDesc());
		jTask.setProviderPersonId(t.getProviderPersonId());
		jTask.setTaskStatus(t.getTaskStatus());
		jTask.setTaskType(t.getTaskType());
		jTask.setTaskName(t.getTaskName());
		jTask.setClientPay(t.getClientPay());
		jTask.setTaskDuration(t.getTaskDuration());
		jTask.setSensedDataFileLocation(t.getSensedDataFileLocation());
		jTask.setAccelerometer(t.getAccelerometer());
		jTask.setGps(t.getGps());
		jTask.setCamera(t.getCamera());
		jTask.setMic(t.getMic());
		jTask.setWifi(t.getWifi());
		jTask.setBluetooth(t.getBluetooth());
		jTask.setMagnetometer(t.getMagnetometer());
		jTask.setProximitySensor(t.getProximitySensor());
		jTask.setAmbientLightSensor(t.getAmbientLightSensor());
		jTask.setTaskAcceptedTime(t.getTaskAcceptedTime());
		jTask.setTaskCompletionTime(t.getTaskCompletionTime());
		jTask.setTaskExpirationTime(t.getTaskExpirationTime());
		jTask.setTaskCreatedTime(t.getTaskCreatedTime());
		return jTask;
	}

	public static long getCurrentTime(){
		return System.currentTimeMillis();
	}
	
	public static Timestamp getTimestamp(){
		return new Timestamp(getCurrentTime());
	}
	
	public static Timestamp getTimestamp(String time){
		SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
		Date lFromDate1 = null;
		try {
			lFromDate1 = datetimeFormatter1.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("formated date :" + lFromDate1);
		Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
		return fromTS1;
	}

	public static String getComplationStatus(Task t) {
		Timestamp exp = t.getTaskExpirationTime();
		Timestamp cur = new Timestamp(Calendar.getInstance().getTime().getTime());
		if(exp.after(cur))
			return "C";
		else
			return "E";
	}

	public static Timestamp getTonightTimestamp() {
		Timestamp now = getTimestamp();
		now.setHours(22);
		now.setMinutes(0);
		now.setNanos(0);
		now.setSeconds(0);
		return now;
	}
}
