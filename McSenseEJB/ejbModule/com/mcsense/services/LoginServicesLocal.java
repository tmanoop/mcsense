package com.mcsense.services;
import javax.ejb.Local;

import com.mcsense.entities.People;

@Local
public interface LoginServicesLocal {

	People loginCheck(String emailId, String password);

	People register(People p);

	boolean isPersonExist(String emailId, String meid);

}
