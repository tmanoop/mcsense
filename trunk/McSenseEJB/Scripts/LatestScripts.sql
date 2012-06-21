select * from SYS.SYSTABLES;

select * from APP.BANK;

select * from APP.PEOPLE;

select * from APP.REPUTATION;

select * from APP.TASK;

SELECT PERSON_ID, PERSON_FNAME, PERSON_ADDRESS, PERSON_LNAME FROM APP.PEOPLE WHERE (PERSON_ID = 1)

select * from APP.TASK where TASK_STATUS = 'C';

update APP.TASK set TASK_STATUS='C' where TASK_STATUS = 'IP';

delete from APP.TASK where TASK_STATUS = 'C';

select * from APP.TASK where TASK_DURATION = 15;
delete from APP.TASK where  TASK_DURATION = 15;

select * from APP.TASK where TASK_ID >= 327;

update APP.TASK set CLIENT_PAY=0.10 where TASK_ID >= 327;

select * from APP.TASK where TASK_ID = 663;
update APP.TASK set TASK_STATUS='IP' where TASK_ID = 663;

select PERSON_ID, EMAILID, PEOPLE.NJIT_DEPARTMENT from PEOPLE;

--reports scripts

select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'bluetooth' and TASK_ACCEPTED_TIME > '2012-03-03 00:00:00';
select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-03-03 00:00:00';
select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'photo' and TASK_ACCEPTED_TIME > '2012-03-05 00:00:00';

select * from APP.TASK where TASK_STATUS = 'E' and TASK_TYPE = 'bluetooth' and TASK_ACCEPTED_TIME > '2012-03-04 00:00:00';
select * from APP.TASK where TASK_STATUS = 'E' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-03-04 00:00:00';
select * from APP.TASK where TASK_STATUS = 'E' and TASK_TYPE = 'photo' and TASK_ACCEPTED_TIME > '2012-03-04 00:00:00';

select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'bluetooth' and TASK_ACCEPTED_TIME > '2012-03-04 00:00:00';
select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-03-04 00:00:00';
select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'photo' and TASK_ACCEPTED_TIME > '2012-03-04 01:00:00';

--active participants
select sum(CLIENT_PAY) as Earnings, PROVIDER_PERSON_ID from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2011-02-27 01:00:00' group by PROVIDER_PERSON_ID order by Earnings;

select CLIENT_PAY, TASK_TYPE from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME > '2011-02-27 01:00:00' and PROVIDER_PERSON_ID = 1353 ;

select TASK_ID, PROVIDER_PERSON_ID from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE <> 'photo' and TASK_ACCEPTED_TIME > '2011-02-27 01:00:00' group by PROVIDER_PERSON_ID, TASK_ID order by PROVIDER_PERSON_ID, TASK_ID;

--in active participants
select sum(CLIENT_PAY) as Loss, PROVIDER_PERSON_ID from APP.TASK where TASK_STATUS = 'E' and TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' group by PROVIDER_PERSON_ID order by Loss;
select sum(CLIENT_PAY) from APP.TASK where TASK_ACCEPTED_TIME > '2012-02-27 01:00:00' and PROVIDER_PERSON_ID = 933;

--Daily accepted tasks
select count(*), TASK_TYPE as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_ACCEPTED_TIME between '2012-02-27 01:00:00' and '2012-02-28 01:00:00' group by TASK_TYPE;

select count(*), TASK_CREATED_TIME as accepted from APP.TASK where TASK_STATUS = 'C' and TASK_CREATED_TIME > '2012-02-27 01:00:00' group by TASK_CREATED_TIME;

--campus tasks
select * from APP.TASK where TASK_STATUS = 'C' and TASK_TYPE = 'campusSensing' and TASK_ACCEPTED_TIME > '2012-02-27 00:00:00';

select * from APP.TASK where TASK_STATUS = 'IP' and TASK_ACCEPTED_TIME > '2012-04-05 01:00:00';

update APP.TASK set TASK_STATUS='E' where TASK_STATUS = 'IP' and TASK_ACCEPTED_TIME > '2012-04-05 01:00:00';

select * from APP.PEOPLE where PERSON_ID = 1251

update APP.PEOPLE set EMAILID = 'ch78@njit.edu' where PERSON_ID = 1251

select * from APP.TASK where LONG_TERM_IND = '1' and TASK_TYPE = 'campusSensing' and TASK_CREATED_TIME > '2012-02-27 01:00:00' and TASK_STATUS = 'C';

select * from APP.TASK where TASK_NAME = 'Task Salsa Night 322159';

update APP.TASK set TASK_NAME = '$5 Task Salsa Night 322158' where TASK_NAME = 'Task Salsa Night 322158';
update APP.TASK set TASK_NAME = '$5 Task Salsa Night 322157' where TASK_NAME = 'Task Salsa Night 322157';
update APP.TASK set TASK_NAME = '$5 Task Salsa Night 322156' where TASK_NAME = 'Task Salsa Night 322156';
update APP.TASK set TASK_NAME = '$5 Task Salsa Night 322155' where TASK_NAME = 'Task Salsa Night 322155';
update APP.TASK set TASK_NAME = '$5 Task Salsa Night 322154' where TASK_NAME = 'Task Salsa Night 322154';

update APP.TASK set TASK_STATUS='C' where TASK_ID in (15365, 15355, 15325, 15310, 15295, 15280, 15115, 15100, 14940, 14915, 14855);

14926

alter table APP.TASK alter TASK_DESC SET DATA TYPE varchar(500);

select * from APP.TASK where TASK_ID = 9159

update APP.TASK set PROVIDER_PERSON_ID=0 where TASK_ID in (14752);

update APP.TASK set TASK_EXPIRATION_TIME= '2012-04-25 10:00:00' where TASK_ID in (15701, 15700, 15698, 15658, 15648);

select * from APP.TASK where TASK_STATUS = 'P' and TASK_TYPE = 'bluetooth' and TASK_ID > 9396
update APP.TASK set TASK_TYPE = 'appUsage' where TASK_STATUS = 'P' and TASK_TYPE = 'bluetooth' and TASK_ID > 9396
delete from APP.TASK where TASK_STATUS = 'P' and TASK_TYPE = 'photo' and TASK_ID > 12316

select * from APP.TASK where TASK_STATUS = 'IP' and TASK_TYPE = 'bluetooth' and TASK_NAME like '$1%'

update APP.TASK set TASK_DESC = 'This is an automated sensing task that collects Bluetooth sensor readings and will earn $1.00 daily.'
where TASK_STATUS = 'IP' and TASK_TYPE = 'bluetooth' and TASK_NAME like '$1%'

update APP.TASK set TASK_STATUS='C' where TASK_STATUS = 'V';

select * from APP.TASK where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' and TASK_NAME like '%Game Room%'

update APP.TASK set TASK_DESC = 'This is an automated sensing task that will earn $2.00 daily. This task is part of a 5-Day task which will end on 04/13/2012.'
where TASK_ID in (12740, 12741, 12742);
where TASK_STATUS in ('IP','P') and TASK_TYPE = 'campusSensing' and TASK_CREATED_TIME > '2012-04-09 01:00:00';

select * from APP.TASK where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' and TASK_NAME like 'Task Flowers%'

update APP.TASK set TASK_NAME = 'Task Flowers 413029'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413029';
update APP.TASK set TASK_NAME = 'Task Flowers 413028'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413028';
update APP.TASK set TASK_NAME = 'Task Flowers 413027'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413027';
update APP.TASK set TASK_NAME = 'Task Flowers 413026'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413026';
update APP.TASK set TASK_NAME = 'Task Flowers 413025'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413025';
update APP.TASK set TASK_NAME = 'Task Flowers 413024'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413024';
update APP.TASK set TASK_NAME = 'Task Flowers 413023'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413023';
update APP.TASK set TASK_NAME = 'Task Flowers 413022'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413022';
update APP.TASK set TASK_NAME = 'Task Flowers 413021'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413021';
update APP.TASK set TASK_NAME = 'Task Flowers 413020'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413020';
update APP.TASK set TASK_NAME = 'Task Flowers 413019'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413019';
update APP.TASK set TASK_NAME = 'Task Flowers 413018'
where TASK_STATUS in ('IP','P','V') and TASK_TYPE = 'photo' and TASK_CREATED_TIME > '2012-04-13 01:00:00' 
and TASK_NAME = 'Task Game Room 413018';