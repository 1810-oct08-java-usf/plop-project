create table ERS_User_Roles(
	ers_user_role_id integer primary key,
	user_role varchar(10)
);

create table ERS_Users(
	ers_user_id integer primary key, 
	ers_username varchar(50), 
	ers_password varchar(50), 
	user_first_name varchar(100), 
	user_last_name varchar(100), 
	user_email varchar(150), 
	user_role_id integer references ERS_User_Roles (ers_user_role_id)
);
create table ERS_Reimbursement_Type(
	reimb_type_id integer primary key,
	reimb_type varchar(10)
);

create table ERS_Reimbursement_Status(
	reimb_status_id integer primary key,
	reimb_status varchar(10)
);

create table ERS_Reimbursement(
	reimb_id serial primary key,
	reimb_amount numeric,
	reimb_submitted timestamp default current_timestamp,
	reimb_resolved timestamp default null,
	reimb_description varchar(250),
	reimb_receipt bytea default null,
	reimb_author integer references ERS_Users(ers_user_id),
	reimb_resolver integer references ERS_Users(ers_user_id) default null,
	reimb_status integer references ERS_Reimbursement_Status(reimb_status_id) default 1,
	reimb_type integer references ERS_Reimbursement_Type(reimb_type_id)
);
drop table ers_reimbursement;
drop table ers_users;

insert into ers_reimbursement_type(reimb_type_id, reimb_type) values
	(1, 'Lodging'),
	(2, 'Travel'),
	(3, 'Food'),
	(4, 'Other');

insert into ers_user_roles(ers_user_role_id, user_role) values
	(1, 'Employee'),
	(2, 'Manager');

insert into ers_reimbursement_status(reimb_status_id, reimb_status) values
	(1, 'Pending'),
	(2, 'Approved'),
	(3, 'Denied');

insert into ers_users (ers_user_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) values
	(1, 'ATanner', 'testpass1', 'Albert', 'Tanner', 'alberttanner@email.com', 2),
	(2, 'BPotter', 'testpass2', 'Betty', 'Potter', 'bettypotter@email.com', 2),
	(3, 'CBaker', 'testpass3', 'Charles', 'Baker', 'charlesbaker@email.com', 1),
	(4, 'DSmith', 'testpass4', 'Derek', 'Smith', 'dereksmith@email.com', 1),
	(5, 'EWeber', 'testpass5', 'Edward', 'Weber', 'edwardweber@email.com', 1),
	(6, 'FCobbler', 'testpass6', 'Florence', 'Cobbler', 'florencecobbler@email.com', 1),
	(7, 'GChandler', 'testpass7', 'George', 'Chandler', 'georgechandler@email.com', 1);
alter table ers_users add salt bytea;
select * from ers_users;


update ers_users set ers_password = 'de3d664945ab688f08098e1e8bf44030' where ers_user_id = 1;
update ers_users set ers_password = '4a2e2f8bf2001011fa70467e3c3121af' where ers_user_id = 2;
update ers_users set ers_password = '77964914a992a51c42432db074cf6bb6' where ers_user_id = 3;
update ers_users set ers_password = 'e3fbfa0cead432f25fa9283531ee40cc' where ers_user_id = 4;
update ers_users set ers_password = 'fc963a41e725f099c9d4675343eecd20' where ers_user_id = 5;
update ers_users set ers_password = '0f477126de429681fb9b47be1d57d03d' where ers_user_id = 6;
update ers_users set ers_password = '48e122f76557c7b62996c97f25f6ece9' where ers_user_id = 7;

ALTER ROLE finance_manager INHERIT LOGIN;
grant update on ers_users to finance_manager;
grant update on ers_reimbursement to finance_manager;
grant select on ers_reimbursement to finance_manager;
grant select on ers_reimbursement to employee;

select salt from ers_users where ers_username = 'CBaker';