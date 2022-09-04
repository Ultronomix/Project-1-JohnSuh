CREATE TABLE ers_reimbursement_statuses (
	status_id varchar,
	status varchar,
	PRIMARY KEY (status_id)
);

ALTER TABLE ers_reimbursement_statuses
ADD UNIQUE(status);

SELECT * FROM ers_reimbursement_statuses;