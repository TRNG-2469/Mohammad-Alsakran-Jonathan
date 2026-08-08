create table Users(
user_id serial primary key,
username varchar(30) not null unique,
password varchar(255) not null,
role boolean not null default false,
first_name varchar(30) not null,
last_name varchar(30) not null,
department_id int references department(department_id) not null
)
--drop table Users cascade
--drop table Reimbursements cascade

create table Department(
department_id serial primary key,
name varchar(40) not null
)

create table Reimbursements(
reimbursements_id serial primary key,
status varchar(20) not null default 'PENDING',
amount numeric(10,2) not null,
description text,
type varchar(20) not null,
resolver_id int references Users(user_id),
author_id int references Users(user_id)
)

