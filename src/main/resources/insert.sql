/**
 * CREATE Script for init of DB
 */

-- Create 3 users
insert into user (id, name, email) values (1, 'John', 'john@example.com');
insert into user (id, name, email) values (2, 'Mike', 'mike@example.com');
insert into user (id, name, email) values (3, 'Carl', 'carl@example.com');

-- Create 3 accounts
insert into account (id, name, user_id, balance) values (1, 'John', 1, 1000.00);
insert into account (id, name, user_id, balance) values (2, 'Mike', 2, 2000.00);
insert into account (id, name, user_id, balance) values (3, 'Carl', 3, 3000.00);