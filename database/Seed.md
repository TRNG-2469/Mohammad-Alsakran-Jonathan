-- seed.md
-- Run after tables.md. Insert order matters: Department, then Users, then Reimbursements.
-- (Users references Department; Reimbursements references Users)

-- Departments
insert into Department (department_id, name) values
(1, 'IT'),
(2, 'Sales'),
(3, 'Finance');

-- Users
-- role: true = Manager, false = Employee

insert into Users (username, password, role, first_name, last_name, department_id) values
('JohnVT', 'password', true, 'John', 'Vermont', 1),
('Sakran2', 'password', false, 'Mohammad', 'Alsakran', 1),
('BobK', 'password', false, 'Bob', 'Kim', 2),
('DanaR', 'password', true, 'Dana', 'Reyes', 2),
('SamP', 'password', false, 'Sam', 'Patel', 3);

-- Reimbursements:
insert into Reimbursements (status, amount, description, type, resolver_id, author_id) values


('PENDING',  1000000.00, 'This is totally real give me money', 'FOOD', null, 1),

-- max value numeric(10,2) can hold: 8 digits before decimal, 2 after
('PENDING',  99999999.99, 'Testing the numeric ceiling', 'OTHER', null, 4),

-- smallest nonzero amount
('PENDING',  0.01, 'A single cent, why not', 'TRAVEL', null, 3),

-- zero amount: should this even be allowed? worth a real discussion
('PENDING',  0.00, 'Zero dollar request', 'OTHER', null, 2),

-- empty description: description is nullable/text, no NOT NULL constraint currently
('PENDING',  25.00, '', 'FOOD', null, 3),

-- very long description, stress-testing the text field (unbounded, should be fine)
('PENDING',  15.00, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, repeated repeated repeated repeated repeated repeated repeated to see how the frontend and API handle a long string', 'LODGING', null, 2),

-- resolved but resolver = author (manager approving their own request, allowed by spec FR9)
('APPROVED', 200.00, 'Manager approving own request', 'TRAVEL', 1, 1),

-- DENIED with a resolver, standard resolved case
('DENIED',   80.00, 'Personal phone bill', 'OTHER', 4, 3),

-- a normal boring one so not everything is an edge case
('APPROVED', 60.00, 'Team lunch', 'FOOD', 4, 3);