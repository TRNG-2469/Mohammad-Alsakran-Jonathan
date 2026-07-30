create table Users(
user_id serial primary key,
username varchar(30) not null,
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
resolver_id int references Users(user_id),
author_id int references Users(user_id)
)

insert into Users ( username, password, role, first_name, last_name, department_id) values
( 'JohnVT', 'password',true , 'John', 'Vermont', 1)

insert into Department (department_id, name) values (1, 'IT')

insert into Reimbursements (status, amount, description, resolver_id, author_id) values
('PENDING', 1000000, 'This is totally real give me money', 1, 1)