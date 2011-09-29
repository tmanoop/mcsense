select * from SYS.SYSTABLES;

select * from APP.BANK;

select * from APP.PEOPLE;

select * from APP.REPUTATION;

select * from APP.TASK;

SELECT PERSON_ID, PERSON_FNAME, PERSON_ADDRESS, PERSON_LNAME FROM APP.PEOPLE WHERE (PERSON_ID = 1)

select * from APP.TASK where TASK_STATUS = 'C';

update APP.TASK set TASK_STATUS='C' where TASK_STATUS = 'IP';

delete from APP.TASK where TASK_STATUS = 'C';