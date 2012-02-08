CREATE TABLE TASK (
		TASK_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1 ,INCREMENT BY 756),
		TASK_STATUS VARCHAR(255),
		TASK_TYPE VARCHAR(255),
		PROVIDER_PERSON_ID INTEGER,
		PARENT_TASK_ID INTEGER,
		CLIENT_PERSON_ID INTEGER,
		TASK_NAME VARCHAR(255),
		TASK_DESC VARCHAR(255),
		TASK_DURATION INTEGER,
		SENSED_DATA_FILE_LOCATION VARCHAR(255),
		ACCELEROMETER VARCHAR(255),
		GPS VARCHAR(255),
		CAMERA VARCHAR(255),
		MIC VARCHAR(255),
		WIFI VARCHAR(255),
		BLUETOOTH VARCHAR(255),
		MAGNETOMETER VARCHAR(255),
		PROXIMITYSENSOR VARCHAR(255),
		AMBIENTLIGHTSENSOR VARCHAR(255),
		TASK_ACCEPTED_TIME TIMESTAMP,
		TASK_COMPLETION_TIME TIMESTAMP,
		TASK_EXPIRATION_TIME TIMESTAMP,
		TASK_CREATED_TIME TIMESTAMP,
		CLIENT_PAY DOUBLE
	);

CREATE INDEX SQL110805224911920 ON TASK (null);

CREATE UNIQUE INDEX SQL110805224911100 ON TASK (null);

ALTER TABLE TASK ADD CONSTRAINT SQL110805224911100 PRIMARY KEY (TASK_ID);

ALTER TABLE TASK ADD CONSTRAINT TASKCLIENTPERSONID FOREIGN KEY (CLIENT_PERSON_ID)
	REFERENCES PEOPLE (PERSON_ID);

