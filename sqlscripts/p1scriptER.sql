CREATE TABLE ers_reimbursements (
	reimb_id varchar,
	amount int NOT NULL,
	submitted timestamp NOT NULL,
	resolved timestamp,
	description varchar NOT NULL,
	receipt bytea,
	payment_id varchar,
	author_id varchar NOT NULL,
	resolver_id varchar,
	status_id varchar NOT NULL,
	type_id varchar,
	PRIMARY KEY (reimb_id)
);

ALTER TABLE ers_reimbursements
ADD CONSTRAINT FK_AuthorUser
FOREIGN KEY (author_id) REFERENCES ers_users (user_id);

ALTER TABLE ers_reimbursements
ADD CONSTRAINT FK_ResolverUser
FOREIGN KEY (resolver_id) REFERENCES ers_users (user_id);

ALTER TABLE ers_reimbursements
ADD CONSTRAINT FK_ReimbursementStatus
FOREIGN KEY (status_id) REFERENCES ers_reimbursement_statuses (status_id);

ALTER TABLE ers_reimbursements
ADD CONSTRAINT FK_ReimbursementType
FOREIGN KEY (type_id) REFERENCES ers_reimbursement_types (type_id);

ALTER TABLE ers_reimbursements 
ALTER COLUMN amount TYPE NUMERIC (6, 2);

SELECT * FROM ers_reimbursements;