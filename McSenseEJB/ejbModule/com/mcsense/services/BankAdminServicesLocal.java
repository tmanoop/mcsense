package com.mcsense.services;
import java.util.List;

import javax.ejb.Local;

import com.mcsense.entities.Bank;
import com.mcsense.entities.People;

@Local
public interface BankAdminServicesLocal {

	List<People> findPersonByName(String name);

	People findPersonByID(String ID);

	int register(People p);

	boolean deletePerson(String id);

	boolean deleteReputation(String id);

	void deposit(Bank b);

	void withdrawal(Bank b);

	void updatePerson(People p);

}
