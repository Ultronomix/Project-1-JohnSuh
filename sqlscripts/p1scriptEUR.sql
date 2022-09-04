CREATE TABLE ers_user_roles (
	role_id varchar,
	role varchar,
	PRIMARY KEY (role_id)
);

ALTER TABLE ers_user_roles
ADD UNIQUE(role);