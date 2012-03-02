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

SELECT TASK_STATUS, CURRENT_TIMESTAMP from APP.TASK 

SELECT DISTINCT CURRENT_TIMESTAMP from APP.TASK;

SELECT CONVERT (char(8), CURRENT_TIMESTAMP, 112) from APP.TASK;

SELECT DISTINCT CONCAT ('photo task', CAST (CURRENT_TIMESTAMP AS CHAR(100))) from APP.TASK;

--reports scripts

select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'bluetooth' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';
select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';
select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'photo' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';

select * from APP.TASK where TASK_STATUS = 'E' and TASK_TYPE = 'bluetooth' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';
select * from APP.TASK where TASK_STATUS = 'E' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';
select * from APP.TASK where TASK_STATUS = 'E' and TASK_TYPE = 'photo' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';

select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'bluetooth' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';
select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-03-01 00:00:00';
select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'photo' and TASK_ACCEPTED_TIME > '2012-03-01 01:00:00';

select sum(CLIENT_PAY) as Earnings, PROVIDER_PERSON_ID from APP.TASK where TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' group by PROVIDER_PERSON_ID order by Earnings;

select sum(CLIENT_PAY) from APP.TASK where TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and PROVIDER_PERSON_ID = 933;