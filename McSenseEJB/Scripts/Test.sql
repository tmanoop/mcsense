select * from SYS.SYSTABLES;

select * from APP.BANK;

select * from APP.PEOPLE;

select * from APP.REPUTATION;

select * from APP.TASK;

SELECT PERSON_ID, PERSON_FNAME, PERSON_ADDRESS, PERSON_LNAME FROM APP.PEOPLE WHERE (PERSON_ID = 1)

select * from APP.TASK where TASK_STATUS = 'C';

update APP.TASK set TASK_STATUS='C' where TASK_STATUS = 'IP';

delete from APP.TASK where TASK_STATUS = 'C';

delete from APP.TASK where TASK_ID >= 386;

select * from APP.TASK where TASK_STATUS = 'IP' and PROVIDER_PERSON_ID = 551 and TASK_EXPIRATION_TIME between '2011-11-14 22:00:00.000' And '2011-11-15 22:00:00.000';