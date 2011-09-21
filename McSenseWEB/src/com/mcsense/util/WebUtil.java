package com.mcsense.util;

import com.mcsense.entities.Task;
import com.mcsense.json.JTask;

public class WebUtil {
	
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
		return jTask;
	}

}
