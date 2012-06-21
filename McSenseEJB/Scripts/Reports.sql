select count(*), TASK_TYPE as Completed from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' group by TASK_TYPE;
select count(*), TASK_TYPE as Errored from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' group by TASK_TYPE;

	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-03-03 01:00:00' and '2012-03-05 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-03-10 01:00:00' and '2012-03-12 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-03-17 01:00:00' and '2012-03-19 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-03-24 01:00:00' and '2012-03-26 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-03-31 01:00:00' and '2012-04-02 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-04-07 01:00:00' and '2012-04-09 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-04-14 01:00:00' and '2012-04-16 01:00:00' group by PROVIDER_PERSON_ID;
	select count(PROVIDER_PERSON_ID), PROVIDER_PERSON_ID from APP.TASK where TASK_CREATED_TIME between '2012-04-21 01:00:00' and '2012-04-23 01:00:00' group by PROVIDER_PERSON_ID;


	--High priced photo tasks
	select count(*) as Completed from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE = 'photo' and CLIENT_PAY > 1.00;
	select count(*) as Errored from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE =	 'photo' and CLIENT_PAY > 1.00;
		
	--high priced sensing tasks
	select count(*) as Completed from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and CLIENT_PAY > 1.00;
	select count(*) as Errored from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and CLIENT_PAY > 1.00;

	--low priced sensing tasks
	select count(*) as Completed from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and CLIENT_PAY <= 1.00;
	select count(*) as Errored from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and CLIENT_PAY <= 1.00;
	
	--long-term sensing tasks
	select count(*) as Completed from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and LONG_TERM_IND = '1';
	select count(*) as Errored from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and LONG_TERM_IND = '1';
	
	--single day sensing tasks
	select count(*) as Completed from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and LONG_TERM_IND IS NULL;
	select count(*) as Errored from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and TASK_TYPE <> 'photo' and LONG_TERM_IND IS NULL;

	--Daily reports

				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-02-27 01:00:00' and '2012-02-28 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-02-28 01:00:00' and '2012-02-29 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-02-29 01:00:00' and '2012-03-01 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-01 01:00:00' and '2012-03-02 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-02 01:00:00' and '2012-03-03 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-03 01:00:00' and '2012-03-04 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-04 01:00:00' and '2012-03-05 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-05 01:00:00' and '2012-03-06 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-06 01:00:00' and '2012-03-07 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-07 01:00:00' and '2012-03-08 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-08 01:00:00' and '2012-03-09 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-09 01:00:00' and '2012-03-10 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-10 01:00:00' and '2012-03-11 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-11 01:00:00' and '2012-03-12 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-12 01:00:00' and '2012-03-13 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-13 01:00:00' and '2012-03-14 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-14 01:00:00' and '2012-03-15 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-15 01:00:00' and '2012-03-16 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-16 01:00:00' and '2012-03-17 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-17 01:00:00' and '2012-03-18 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-18 01:00:00' and '2012-03-19 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-19 01:00:00' and '2012-03-20 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-20 01:00:00' and '2012-03-21 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-21 01:00:00' and '2012-03-22 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-22 01:00:00' and '2012-03-23 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-23 01:00:00' and '2012-03-24 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-24 01:00:00' and '2012-03-25 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-25 01:00:00' and '2012-03-26 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-26 01:00:00' and '2012-03-27 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-27 01:00:00' and '2012-03-28 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-28 01:00:00' and '2012-03-29 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-29 01:00:00' and '2012-03-30 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-30 01:00:00' and '2012-03-31 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-03-31 01:00:00' and '2012-04-01 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-01 01:00:00' and '2012-04-02 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-02 01:00:00' and '2012-04-03 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-03 01:00:00' and '2012-04-04 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-04 01:00:00' and '2012-04-05 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-05 01:00:00' and '2012-04-06 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-06 01:00:00' and '2012-04-07 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-07 01:00:00' and '2012-04-08 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-08 01:00:00' and '2012-04-09 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-09 01:00:00' and '2012-04-10 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-10 01:00:00' and '2012-04-11 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-11 01:00:00' and '2012-04-12 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-12 01:00:00' and '2012-04-13 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-13 01:00:00' and '2012-04-14 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-14 01:00:00' and '2012-04-15 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-15 01:00:00' and '2012-04-16 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-16 01:00:00' and '2012-04-17 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-17 01:00:00' and '2012-04-18 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-18 01:00:00' and '2012-04-19 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-19 01:00:00' and '2012-04-20 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-20 01:00:00' and '2012-04-21 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-21 01:00:00' and '2012-04-22 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-22 01:00:00' and '2012-04-23 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-23 01:00:00' and '2012-04-24 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-24 01:00:00' and '2012-04-25 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-25 01:00:00' and '2012-04-26 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-26 01:00:00' and '2012-04-27 01:00:00' group by TASK_TYPE;
				select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-04-27 01:00:00' and '2012-04-28 01:00:00' group by TASK_TYPE;