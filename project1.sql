
/*******************************************************************************
   Description: Creates and populates the Expense Reimbursement System database.
   DB Server: Oracle
   Author: Nigel Williams
********************************************************************************/

/*******************************************************************************
   Drop database if it exists
********************************************************************************/
DROP USER ers_app CASCADE;
DROP USER ers_user CASCADE;
DROP USER ers_read_only CASCADE;

GRANT CONNECT TO ers_app;
GRANT RESOURCE TO ers_app;
GRANT CREATE SESSION TO ers_app;
GRANT CREATE TABLE TO ers_app;
GRANT CREATE VIEW TO ers_app;


/*******************************************************************************
   Create database
********************************************************************************/
CREATE USER ers_user 
IDENTIFIED BY p4ssword;

GRANT CONNECT TO ers_user;
GRANT CREATE SESSION TO ers_user;
GRANT SELECT, INSERT, UPDATE ON ers_app.ers_users TO ers_user;
GRANT SELECT ON ers_app.ers_user_roles TO ers_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ers_app.ers_reimbursement_status TO ers_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ers_app.ers_reimbursement_type TO ers_user;

CREATE USER ers_read_only
IDENTIFIED BY readOnly;

GRANT CONNECT TO ers_read_only;
GRANT CREATE SESSION TO ers_read_only;
GRANT SELECT, INSERT ON ers_app.ers_users TO ers_read_only;
GRANT SELECT ON ers_app.ers_user_roles TO ers_read_only;


CREATE USER ers_app
IDENTIFIED BY Onelove00
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT connect to ers_app;
GRANT resource to ers_app;
GRANT create session TO ers_app;
GRANT create table TO ers_app;
GRANT create view TO ers_app;



conn ers_app/Onelove00


/*******************************************************************************
   Create Tables
********************************************************************************/
CREATE TABLE ERS_REIMBURSEMENT_STATUS
(
   REIMB_STATUS_ID   NUMBER,
   REIMB_STATUS   VARCHAR2(10),
   
   CONSTRAINT REIMB_STATUS_PK
   PRIMARY KEY (REIMB_STATUS_ID)
   
);


CREATE TABLE ERS_REIMBURSEMENT_TYPE
(
    REIMB_TYPE_ID  NUMBER,
    REIMB_TYPE VARCHAR2(10),
    
    CONSTRAINT REIMB_TYPE_PK
    PRIMARY KEY(REIMB_TYPE_ID)
);

CREATE TABLE ERS_USER_ROLES
(
  ERS_USER_ROLE_ID  NUMBER,
  USER_ROLE  VARCHAR2(10),
  
  CONSTRAINT ERS_USER_ROLE_PK
  PRIMARY KEY (ERS_USER_ROLE_ID)

);



CREATE TABLE ERS_USERS
(
  ERS_USER_ID  NUMBER,
  ERS_USERNAME VARCHAR2(50) UNIQUE,
  ERS_PASSWORD VARCHAR2(50),
  USER_FIRST_NAME VARCHAR2(100),
  ERS_LAST_NAME VARCHAR2(100),
  USER_EMAIL VARCHAR(150) UNIQUE,
  USER_ROLE_ID NUMBER,
  
  CONSTRAINT ERS_USER_PK
  PRIMARY KEY (ERS_USER_ID)

);

ALTER TABLE ERS_USERS
ADD CONSTRAINT USER_ROLES_FK
FOREIGN KEY(USER_ROLE_ID)
REFERENCES ERS_USER_ROLES (ERS_USER_ROLE_ID);

CREATE TABLE ERS_REIMBURSEMENT
(
   REIMB_ID NUMBER,
   REIMB_AMOUNT NUMBER,
   REIMB_SUBMITTED TIMESTAMP,
   REIMB_RESOLVED TIMESTAMP,
   REIMB_DESCRIPTION VARCHAR2(250),
   REIMB_RECEIPT BLOB,
   REIMB_AUTHOR NUMBER,
   REIMB_RESOLVER NUMBER,
   REIMB_STATUS_ID NUMBER,
   REIMB_TYPE_ID NUMBER,
   
   CONSTRAINT ERS_REIMBURSEMENT_PK
   PRIMARY KEY (REIMB_ID)

);
ALTER TABLE ERS_REIMBURSEMENT
MODIFY REIMB_STATUS_ID DEFAULT 2;

ALTER TABLE ERS_REIMBURSEMENT
MODIFY REIMB_RESOLVER DEFAULT 0;

ALTER TABLE ERS_REIMBURSEMENT
MODIFY REIMB_AMOUNT NUMBER(7,2);


ALTER TABLE ERS_REIMBURSEMENT
ADD CONSTRAINT ERS_USERS_FK_AUTH
FOREIGN KEY(REIMB_AUTHOR)
REFERENCES ERS_USERS (ERS_USER_ID);


ALTER TABLE ERS_REIMBURSEMENT
ADD CONSTRAINT ERS_USERS_FK_RESLVR
FOREIGN KEY(REIMB_RESOLVER)
REFERENCES ERS_USERS (ERS_USER_ID);


ALTER TABLE ERS_REIMBURSEMENT
ADD CONSTRAINT ERS_REIMBURSEMENT_STATUS_FK
FOREIGN KEY(REIMB_STATUS_ID)
REFERENCES ERS_REIMBURSEMENT_STATUS (REIMB_STATUS_ID);



ALTER TABLE ERS_REIMBURSEMENT
ADD CONSTRAINT ERS_REIMBURSEMENT_TYPE_FK
FOREIGN KEY(REIMB_TYPE_ID)
REFERENCES ERS_REIMBURSEMENT_TYPE (REIMB_TYPE_ID);
-------------------------------------------------------------------------------------------------------------------------------
--                                                    TRIGGERS AND SEQUNCES
--------------------------------------------------------------------------------------------------------------------------------
DROP SEQUENCE ers_reimbursement_pk_seq;
DROP SEQUENCE ers_users_pk_seq;
DROP TRIGGER ers_users_pk_trigger;
DROP TRIGGER ers_reimbursement_pk_trigger;


CREATE SEQUENCE ers_users_pk_seq
MINVALUE 1
MAXVALUE 99999999
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE ers_reimbursement_pk_seq
MINVALUE 1
MAXVALUE 99999999
INCREMENT BY 1
START WITH 1;


CREATE OR REPLACE TRIGGER ers_users_pk_trigger
BEFORE INSERT ON ers_app.ers_users
FOR EACH ROW
BEGIN
    SELECT ers_users_pk_seq.NEXTVAL
    INTO :new.ers_user_id
    FROM dual;
END;
/

CREATE OR REPLACE TRIGGER ers_reimbursement_pk_trigger
BEFORE INSERT ON ers_app.ers_reimbursement
FOR EACH ROW
BEGIN
    SELECT ers_reimbursement_pk_seq.NEXTVAL
    INTO :new.reimb_id
    FROM dual;
END;
/




CREATE OR REPLACE TRIGGER set_time
   BEFORE INSERT
   ON ers_app.ers_reimbursement
   FOR EACH ROW
BEGIN
    
         SELECT sysdate
           INTO :new.reimb_submitted
           FROM dual;
 
END;
/
CREATE OR REPLACE TRIGGER update_time
   BEFORE UPDATE
   ON ers_app.ers_reimbursement
   FOR EACH ROW
BEGIN
      SELECT sysdate
        INTO :new.reimb_resolved
        FROM dual;
END;
/
CREATE OR REPLACE TRIGGER set_author_fk_trigger
BEFORE INSERT ON ers_app.ers_reimbursement
FOR EACH ROW
BEGIN
    SELECT ers_user_id
    INTO :new.reimb_author
    FROM ers_users;
END;
/
CREATE OR REPLACE TRIGGER set_resolver_fk_trigger
BEFORE UPDATE ON ers_app.ers_reimbursement
FOR EACH ROW
BEGIN
    SELECT ers_user_id
    INTO :new.reimb_resolver
    FROM ers_users;
END;
/

--------------------------------------------------------------------------------------------------------------------------------
--                                                 POPULATE TABLES                   
--------------------------------------------------------------------------------------------------------------------------------
INSERT INTO ers_user_roles Values(1, 'ADMIN');
INSERT INTO ers_user_roles Values(2, 'DEV');
INSERT INTO ers_user_roles Values(3, 'REG');
INSERT INTO ers_user_roles Values(4, 'LOCKED');

INSERT INTO ers_reimbursement_status Values(1, 'APPROVED');
INSERT INTO ers_reimbursement_status Values(2, 'PENDING');
INSERT INTO ers_reimbursement_status Values(3, 'DENIED');

INSERT INTO ers_reimbursement_type Values(1, 'LODGING');
INSERT INTO ers_reimbursement_type Values(2, 'TRAVEL');
INSERT INTO ers_reimbursement_type Values(3, 'FOOD');
INSERT INTO ers_reimbursement_type Values(4, 'OTHER');

INSERT INTO ers_users  VALUES (1, 'nwilliams', 'p4ssw0rd','Nigel', 'Williams',  'nwilliams@gmail.com', 1);
INSERT INTO ers_users  VALUES (2, 'cmieato', 'p1ssw0rd', 'Ciara', 'Mieato', 'cmieato@gmail.com', 1);
INSERT INTO ers_users  VALUES (3, 'snavy', 'p2ssw0rd', 'Shantoy', 'Navy', 'snavy@yahoo.com', 3);
INSERT INTO ers_users  VALUES (4, 'adesiel', 'p3ssw0rd', 'Allure', 'Desiel',  'adesiel@gmail.com', 3);
INSERT INTO ers_users  VALUES (5, 'jnotchan', 'p0ssw0rd', 'Jackie', 'Notchan',  'jnotchan@outlook.com', 3);

INSERT INTO ers_reimbursement  VALUES (1, 900.09, LOCALTIMESTAMP(2), NULL, 'FOOD EXPENSE',NULL, 1, 2, 2, 3);
INSERT INTO ers_reimbursement  VALUES (2, 922.01, LOCALTIMESTAMP(2), NULL, 'FOOD EXPENSE',NULL, 1, 2, 2, 3 );
INSERT INTO ers_reimbursement  VALUES (3, 933.05, LOCALTIMESTAMP(2), NULL, 'FOOD EXPENSE',NULL, 1, 2, 2, 3);
INSERT INTO ers_reimbursement  VALUES (4, 944.95, LOCALTIMESTAMP(2), NULL, 'FOOD EXPENSE',NULL, 3, 2, 2, 3);
INSERT INTO ers_reimbursement  VALUES (5, 955.78, NULL, NULL, 'FOOD EXPENSE',NULL, 1, 2, 2, 3);


TRUNCATE TABLE ERS_REIMBURSEMENT;
COMMIT;

SELECT *
FROM ers_reimbursement;
--------------------------------------------------------------------------------------------------------------------------------
--                                                 FUNCTIONS AND PROCEDURES FOR USERS
--------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE get_all_users
    (
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM ers_users
    ORDER BY ers_user_id;
END;
/



CREATE OR REPLACE PROCEDURE get_users_by_id
    (
        user_id  IN ers_users.ers_user_id%TYPE,
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM ers_users
    WHERE ers_user_id = user_id
    ORDER BY ers_user_id;
END;
/
--------------------------------------------------------------------------------------------------------------------------------
--                                            FUNCTIONS AND PROCEDURES FOR REIMBURSEMENT
--------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE get_all_reimbursement
    (
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM ers_reimbursement
    ORDER BY reimb_status_id;
END;
/

CREATE OR REPLACE PROCEDURE get_reimb_by_id
    (
        rmb_id  IN ers_reimbursement.reimb_id%TYPE,
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM ers_reimbursement
    WHERE reimb_author = rmb_id
    ORDER BY reimb_status_id;
END;
/
CREATE OR REPLACE PROCEDURE ers_reimb_update
    (
        ticket_id IN ers_reimbursement.reimb_id%TYPE,
        author_id IN ers_reimbursement.reimb_author%TYPE,
        resolver_id IN ers_reimbursement.reimb_resolver%TYPE,
        status IN ers_reimbursement.reimb_status_id%TYPE
    )
IS
   
   
BEGIN
   IF author_id != resolver_id THEN
      UPDATE ers_reimbursement SET reimb_status_id = status
         WHERE reimb_author = author_id
         AND reimb_id = ticket_id;
    UPDATE ers_reimbursement SET reimb_resolver = resolver_id
         WHERE reimb_author = author_id
         AND reimb_id = ticket_id;
         DBMS_OUTPUT.PUT_LINE('Transaction Complete...updating :)');
   ELSE
    rollback;
      DBMS_OUTPUT.PUT_LINE('Something went wrong rolling back :(');
   END IF;
   COMMIT;
END;
/
select *
from ers_reimbursement;

exec ers_reimb_update(44,1,3,3);

CREATE OR REPLACE PROCEDURE create_reimb
    ( 
       amt IN ers_reimbursement.reimb_amount%TYPE,
       descript IN ers_reimbursement.reimb_description%TYPE,
       author IN ers_reimbursement.reimb_author%TYPE,
       typ IN ers_reimbursement.reimb_type_id%TYPE
    )
IS
BEGIN
    
    INSERT INTO ers_reimbursement  (reimb_amount, reimb_description, reimb_author, reimb_type_id)
    VALUES (amt, descript, author, typ);
    
END;
/














