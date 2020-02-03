insert into roles (id, name) values (1, 'ADMIN');
insert into roles (id, name) values (2, 'USER');

insert into user (id, login, name, password) values (1, 'admin', 'tomato', '123');
insert into user (id, login, name, password) values (2, 'user', 'ifactor', '123');
insert into user (id, login, name, password) values (3, 'guest1', 'guest', '123');
insert into user (id, login, name, password) values (4, 'guest2', 'guest', '123');
insert into user (id, login, name, password) values (5, 'guest3', 'guest', '123');
insert into user (id, login, name, password) values (6, 'guest4', 'guest', '123');
insert into user (id, login, name, password) values (7, 'guest5', 'guest', '123');

insert into user_roles (user_id, role_id) values (1, 1);
insert into user_roles (user_id, role_id) values (1, 2);
insert into user_roles (user_id, role_id) values (2, 2);

insert into user_roles (user_id, role_id) values (3, 2);
insert into user_roles (user_id, role_id) values (4, 2);
insert into user_roles (user_id, role_id) values (5, 2);
insert into user_roles (user_id, role_id) values (6, 2);
insert into user_roles (user_id, role_id) values (7, 2);