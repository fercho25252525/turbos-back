/* Creamos algunos usuarios con sus roles */
INSERT INTO `user` (user_name, password, enabled, name, last_name, email) values('andres','$2a$10$C3Uln5uqnzx/GswADURJGOIdBqYrly9731fnwKDaUdBkt/M3qvtLq',1, 'Andres', 'Guzman','profesor@bolsadeideas.com');
INSERT INTO `user` (user_name, password, enabled, name, last_name, email) VALUES ('admin','$2a$10$RmdEsvEfhI7Rcm9f/uZXPebZVCcPC7ZXZwV51efAvMAp1rIaRAfPK',1, 'John', 'Doe','jhon.doe@bolsadeideas.com');

INSERT INTO `roles` (name) VALUES ('ROLE_USER');
INSERT INTO `roles` (name) VALUES ('ROLE_ADMIN');

INSERT INTO `users_roles` (user_id, role_id) VALUES ("andres", 1);
INSERT INTO `users_roles` (user_id, role_id) VALUES ("admin", 2);
INSERT INTO `users_roles` (user_id, role_id) VALUES ("admin", 1);