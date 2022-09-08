CREATE TABLE ers_user_roles (
	role_id varchar,
	role varchar,
	PRIMARY KEY (role_id)
);

ALTER TABLE ers_user_roles
ADD UNIQUE(role);

SELECT * FROM ers_user_roles;

ALTER TABLE ers_user_roles
ALTER role
SET DEFAULT 'EMPLOYEE';

ALTER TABLE ers_user_roles 
ALTER role_id
SET DEFAULT gen_random_uuid();

ALTER TABLE ers_user_roles 
ALTER role_id TYPE uuid
USING role_id::uuid;

INSERT INTO ers_user_roles (role)
VALUES ('ADMIN');

INSERT INTO ers_user_roles (role)
VALUES ('FINANCE MANAGER');

INSERT INTO ers_user_roles (role)
VALUES ('EMPLOYEE');