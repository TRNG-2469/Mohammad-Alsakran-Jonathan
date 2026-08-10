-- seed.md
-- Run after tables.md.

-- Departments
insert into Department (name) values
('IT'),
('Sales'),
('Finance');

-- Reimbursements
-- NOTE: run this only after Users have been created via the API (see seed_users.md),
-- since author_id/resolver_id here assume specific user_ids that only exist post-registration.
insert into Reimbursements (status, amount, description, type, resolver_id, author_id) values
('PENDING',  1000000.00, 'This is totally real give me money', 'FOOD', null, 1),
('PENDING',  99999999.99, 'Testing the numeric ceiling', 'OTHER', null, 4),
('PENDING',  0.01, 'A single cent, why not', 'TRAVEL', null, 3),
('PENDING',  0.00, 'Zero dollar request', 'OTHER', null, 2),
('PENDING',  25.00, '', 'FOOD', null, 3),
('PENDING',  15.00, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, repeated repeated repeated repeated repeated repeated repeated to see how the frontend and API handle a long string', 'LODGING', null, 2),
('APPROVED', 200.00, 'Manager approving own request', 'TRAVEL', 1, 1),
('DENIED',   80.00, 'Personal phone bill', 'OTHER', 4, 3),
('APPROVED', 60.00, 'Team lunch', 'FOOD', 4, 3);