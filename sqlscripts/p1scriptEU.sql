CREATE TABLE ers_users (
	user_id varchar,
	username varchar UNIQUE NOT NULL,
	email varchar UNIQUE NOT NULL,
	password varchar NOT NULL,
	given_name varchar NOT NULL,
	surname varchar NOT NULL,
	is_active boolean,
	role_id varchar,
	PRIMARY KEY (user_id)
);

ALTER TABLE ers_users
ADD CONSTRAINT FK_UserRoles
FOREIGN KEY (role_id) REFERENCES ers_user_roles (role_id);