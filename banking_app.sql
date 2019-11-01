
CREATE TABLE Accountx
(
    Id NUMBER NOT NULL,
    AccountNumber NUMBER(6) NOT NULL,
    AccountType VARCHAR2(25) DEFAULT 'CHECKING' ,
    Balance NUMBER (7,2) DEFAULT 0.00, 
    CONSTRAINT PK_Accountx
    PRIMARY KEY (AccountNumber),
    
   CONSTRAINT FK_Userx
   FOREIGN KEY (Id)
 REFERENCES Userx (Id)

);
ALTER TABLE ACCOUNTX
 
 DROP TABLE ACCOUNTX;
 
TRUNCATE TABLE USERX;

ALTER TABLE ACCOUNTX
DROP CONSTRAINT FK_Userx;

DROP TABLE ACCOUNTX;
TRUNCATE TABLE USERX;


ALTER TABLE ACCOUNTX
MODIFY USERID DEFAULT userx_pk_trigger.nex;

ALTER TABLE ACCOUNTX
MODIFY ACCOUNTNUMBER DEFAULT '0';

ALTER TABLE ACCOUNTX
MODIFY ACCOUNTTYPE DEFAULT 'CHECKING';

ALTER TABLE ACCOUNTX
MODIFY BALANCE DEFAULT '0.00';

ALTER TABLE ACCOUNTX
MODIFY USERID DROP DEFAULT;

CREATE TABLE Userx
(
    Id NUMBER,
    FirstName VARCHAR2(40) NOT NULL,
    LastName VARCHAR2(20) NOT NULL,
    Username VARCHAR2(40) UNIQUE NOT NULL,
    Password VARCHAR2(25) NOT NULL,
    Address VARCHAR2(70),
    City VARCHAR2(40),
    State VARCHAR2(40),
    Country VARCHAR2(40),
    PostalCode VARCHAR2(10),
    Phone VARCHAR2(24),
    Email VARCHAR2(60) NOT NULL,
    CONSTRAINT PK_Userx 
    PRIMARY KEY (Id)
);


INSERT INTO Accountx VALUES (1, 009001, 'Checking',  8000.60);
INSERT INTO Accountx VALUES (2, 009441, 'Saving', 10000.10);
INSERT INTO Accountx VALUES (3, 009551, 'Checking', 500.10);
INSERT INTO Accountx VALUES (4, 009331, 'Checking', 350.90);
INSERT INTO Accountx VALUES (5, 009221, 'Saving', 9085.20);



INSERT INTO Userx (Id, FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email) VALUES (1, 'Nigel', 'Williams', 'nwilliams', 'p4ssw0rd','100-231 Street', 'Jamaica', 'FL', 'USA', '31606', '+1 (201) 298-0987', 'nwilliams@gmail.com');
INSERT INTO Userx (Id, FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email) VALUES (2, 'Ciara', 'Mieato', 'cmieato', 'p1ssw0rd','101-200 Street', 'Confuse City', 'FL', 'USA', '31607', '+1 (201) 298-0988', 'cmieato@gmail.com');
INSERT INTO Userx (Id, FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email) VALUES (3, 'Shantoy', 'Navy', 'snavy', 'p2ssw0rd','110-239 Street', 'Jamrock', 'FL', 'USA', '31608', '+1 (201) 298-0887', 'snavy@yahoo.com');
INSERT INTO Userx (Id, FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email) VALUES (4, 'Allure', 'Desiel', 'adesiel', 'p3ssw0rd','111-211 Street', 'Tampa', 'FL', 'USA', '31609', '+1 (201) 298-6987', 'adesiel@gmail.com');
INSERT INTO Userx (Id, FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email) VALUES (5, 'Jackie', 'Notchan', 'jnotchan', 'p0ssw0rd','000-201 Street', 'Sea View', 'FL', 'USA', '31000', '+1 (201) 666-0987', 'jnotchan@outlook.com');


commit;
exit

DROP SEQUENCE accountx_pk_seq;
DROP SEQUENCE accountx_fk_seq;
DROP SEQUENCE userx_pk_seq;


CREATE SEQUENCE userx_pk_seq
MINVALUE 1
MAXVALUE 99999999
INCREMENT BY 1
START WITH 6;

CREATE SEQUENCE accountx_fk_seq
MINVALUE 1
MAXVALUE 99999999
INCREMENT BY 1
START WITH 6;


CREATE SEQUENCE accountx_pk_seq
MINVALUE 009552
MAXVALUE 99999999
INCREMENT BY 1
START WITH 009552;

CREATE OR REPLACE TRIGGER accountx_pk_trigger
BEFORE INSERT ON accountx
FOR EACH ROW
BEGIN
    SELECT accountx_pk_seq.NEXTVAL
    INTO :new.AccountNumber
    FROM dual;
END;
/

CREATE OR REPLACE TRIGGER userx_pk_trigger
BEFORE INSERT ON userx
FOR EACH ROW
BEGIN
    SELECT userx_pk_seq.NEXTVAL
    INTO :new.id
    FROM dual;
END;
/

CREATE OR REPLACE TRIGGER accountx_fk_trigger
BEFORE INSERT ON accountx
FOR EACH ROW
BEGIN
    SELECT userx_pk_seq.nextval-1
    INTO :new.id
    FROM dual;
END;
/

CREATE OR REPLACE PROCEDURE get_all_accountx
    (
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM accountx
    ORDER BY UserId;
END;
/
CREATE OR REPLACE PROCEDURE get_all_userx
    (
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM userx
    ORDER BY Id;
END;
/

CREATE OR REPLACE PROCEDURE get_accountx_by_id
    (
        accid  IN accountx.Id%TYPE,
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM accountx
    WHERE Id = accid
    ORDER BY Balance;
END;
/

CREATE OR REPLACE PROCEDURE withdraw
    (
       accid IN accountx.Id%TYPE,
       debit  IN accountx.Balance%TYPE,
        results OUT accountx.balance%TYPE
    )
    IS
BEGIN
   SELECT balance INTO results
   FROM accountx
      WHERE Id = accid;

   IF results >= debit AND debit < 2500 THEN
      UPDATE accountx SET balance = balance - debit
         WHERE Id = accid;
         results := results - debit;
         DBMS_OUTPUT.PUT_LINE('Transaction Successful...Take your money :)'); 
   ELSE
    rollback;
      DBMS_OUTPUT.PUT_LINE('Insufficient funds...Your daily limit is $2500 :(');
   END IF;
   COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE deposit
    (
        accid IN accountx.Id%TYPE,
        credit  IN accountx.Balance%TYPE,
        results OUT accountx.balance%TYPE
    )
    IS
BEGIN
   SELECT balance INTO results
   FROM accountx
      WHERE Id = accid
      FOR UPDATE OF balance;
   IF credit > 0 AND credit < 10000 THEN
      UPDATE accountx SET balance = balance + credit
         WHERE Id = accid;
         results := results + credit;
         DBMS_OUTPUT.PUT_LINE('Transaction Sucessful...Your money was deposited :)');
   ELSE
    rollback;
      DBMS_OUTPUT.PUT_LINE('You cannot make deposits of less than zero...You cannot make deposit of more than $10,000 :(');
   END IF;
   COMMIT;
END;
/








CREATE OR REPLACE PROCEDURE get_accountx_by_id
(
 in_id IN accountx.Id%TYPE,
 out_acc OUT accountx.accountnumber%TYPE,
 out_type OUT accountx.accounttype%TYPE,
 out_balance OUT accountx.balance%TYPE
 )
AS
BEGIN
  SELECT AccountNumber, AccountType, Balance
  INTO out_acc, out_type, out_balance
  FROM accountx
  WHERE Id = in_id;
  
END;
/

CREATE OR REPLACE PROCEDURE user_account
    (
        my_cursor OUT SYS_REFCURSOR
    )
IS
BEGIN
    OPEN my_cursor FOR
    SELECT *
    FROM Accountx
    JOIN Userx
    ON Id = UserId;
END;
/


--This Not Used Prevents Accounts From Being Deleted
CREATE OR REPLACE TRIGGER block_accounts_delete_trigger   
  BEFORE DELETE ON Accountx     
  FOR EACH ROW 
  DECLARE

BEGIN
  
 raise_application_error(-20001,'Records can not be deleted');
END;
/
DROP TRIGGER block_accounts_delete_trigger;
DROP TRIGGER accountx_fk_trigger;
DROP TRIGGER userx_pk_trigger;
DROP TRIGGER accountx_pk_trigger;

DELETE
    FROM uSERx
     WHERE ID > 5;
     
     
INSERT INTO ACCOUNTX VALUES (null,null,null,null);
INSERT INTO Userx (Id, FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email) VALUES (1, 'Nigel', 'Williams', 'nwilliams', 'p4ssw0rd','100-231 Street', 'Jamaica', 'FL', 'USA', '31606', '+1 (201) 298-0987', 'nwilliams@gmail.com');
CREATE OR REPLACE PROCEDURE user_account
    ( 
       in_fn userx.firstname%TYPE,
       ln userx.lastname%TYPE, 
       un userx.username%TYPE, 
       pw userx.password%TYPE, 
       Addr userx.address%TYPE, 
       cty userx.city%TYPE, 
       st userx.state%TYPE, 
       cnt userx.country%TYPE,
       pc userx.postalcode%TYPE,
       p userx.phone%TYPE,
       e userx.email%TYPE,
       aid accountx.id%TYPE,
       anum accountx.accountnumber%TYPE,
       ta accountx.accounttype%TYPE,
       bal accountx.balance%TYPE
       
    )
IS
BEGIN
    INSERT INTO USERX (FirstName, LastName, Username, Password, Address, City, State, Country, PostalCode, Phone, Email)
    VALUES(in_fn,ln, un, pw, Addr, cty, st, cnt,pc ,p ,e ,aid);
     SET aid = userx.id;
     
     INSERT INTO ACCOUNTX VALUES (aid, anum, ta, bal);
    
END;
/
prompt C R E A T E   N E W  USER   R E C O R D
prompt
prompt Enter your information:
prompt
accept l_ename char format a10 prompt 'First name: '



delete from userx where id > 5;

