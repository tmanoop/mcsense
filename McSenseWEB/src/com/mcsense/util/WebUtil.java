package com.mcsense.util;

import com.mcsense.entities.Task;
import com.mcsense.json.JTask;

public class WebUtil {
	
	public static JTask mapToJsonTask(Task t) {
		JTask jTask = new JTask(t.getTaskId(),t.getTaskName());
		jTask.setProviderPersonId(t.getProviderPersonId());
		jTask.setTaskStatus(t.getTaskStatus());
		jTask.setTaskType(t.getTaskType());
		jTask.setTaskName(t.getTaskName());
		return jTask;
	}

}
