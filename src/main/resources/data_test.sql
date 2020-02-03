delete from user_roles;
delete from user;
delete from roles;

insert into roles (id, name) values (1, 'ADMIN');
insert into roles (id, name) values (2, 'USER');

insert into user (id, login, name, password) values (1, 'admin', 'tomato', '123');
insert into user (id, login, name, password) values (2, 'user', 'ifactor', '123');

insert into user_roles (user_id, role_id) values (1, 1);
insert into user_roles (user_id, role_id) values (1, 2);
insert into user_roles (user_id, role_id) values (2, 2);