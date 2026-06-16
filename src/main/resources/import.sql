INSERT INTO roles(id,name) VALUES(1,'ROLE_USER'),(2,'ROLE_MODERATOR'),(3,'ROLE_ADMIN');

insert into users (id, email, password, username) values (1, 'vasi@gmail.com', '$2a$10$8OSE2K3BO5M04O747Fa1WO0vBnUrtOfiyPdudRlo6fxtcU12uXCBe', 'vasi');
insert into user_roles (user_id, role_id) values (1, 1) ,(1, 2),(1, 3);

insert into users (id, email, password, username) values (2, 'anna@gmail.com', '$2a$10$qCAyLwVUCTNmfvq6ozg8vuIc6QQAr4j26STBZbA7uaBBDLE3g5PAy', 'anna');
insert into user_roles (user_id, role_id) values (2, 1) ;