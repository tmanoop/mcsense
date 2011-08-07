package com.mcsense.services;
import javax.ejb.Local;

import com.mcsense.entities.Task;

@Local
public interface TaskServicesLocal {

	Task createTask(Task t);

}
