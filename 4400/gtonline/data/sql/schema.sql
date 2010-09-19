CREATE TABLE account (id BIGINT AUTO_INCREMENT, email VARCHAR(255) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, firstname VARCHAR(255) NOT NULL, lastname VARCHAR(255) NOT NULL, accounttype VARCHAR(255) NOT NULL, PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE administrator (id BIGINT AUTO_INCREMENT, accountid BIGINT NOT NULL, lastlogin datetime NOT NULL, INDEX accountid_idx (accountid), PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE employer (id BIGINT AUTO_INCREMENT, name VARCHAR(255) NOT NULL UNIQUE, PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE relationship (sourceuserid BIGINT, destinationuserid BIGINT, description VARCHAR(255), accepttime DATE, status VARCHAR(255) NOT NULL, PRIMARY KEY(sourceuserid, destinationuserid)) ENGINE = INNODB;
CREATE TABLE school (id BIGINT AUTO_INCREMENT, name VARCHAR(255) NOT NULL UNIQUE, type VARCHAR(255) NOT NULL, PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE status_update (id BIGINT AUTO_INCREMENT, userid BIGINT NOT NULL, created_at datetime NOT NULL, text TEXT NOT NULL, INDEX userid_idx (userid), PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE status_update_comment (id BIGINT AUTO_INCREMENT, statusupdateid BIGINT NOT NULL, created_at datetime NOT NULL, text TEXT NOT NULL, userid BIGINT NOT NULL, INDEX userid_idx (userid), INDEX statusupdateid_idx (statusupdateid), PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE user (id BIGINT AUTO_INCREMENT, accountid BIGINT NOT NULL, sex BIGINT NOT NULL, birthday DATE NOT NULL, currentcity VARCHAR(255), hometown VARCHAR(255), INDEX accountid_idx (accountid), PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE user_employer (id BIGINT AUTO_INCREMENT, userid BIGINT NOT NULL, employerid BIGINT NOT NULL, jobtitle VARCHAR(255), INDEX userid_idx (userid), INDEX employerid_idx (employerid), PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE user_interest (id BIGINT AUTO_INCREMENT, userid BIGINT NOT NULL, description VARCHAR(255) NOT NULL, INDEX userid_idx (userid), PRIMARY KEY(id)) ENGINE = INNODB;
CREATE TABLE user_school (id BIGINT AUTO_INCREMENT, userid BIGINT NOT NULL, schoolid BIGINT NOT NULL, graduationyear BIGINT, INDEX userid_idx (userid), INDEX schoolid_idx (schoolid), PRIMARY KEY(id)) ENGINE = INNODB;
ALTER TABLE administrator ADD CONSTRAINT administrator_accountid_account_id FOREIGN KEY (accountid) REFERENCES account(id);
ALTER TABLE relationship ADD CONSTRAINT relationship_sourceuserid_user_id FOREIGN KEY (sourceuserid) REFERENCES user(id);
ALTER TABLE relationship ADD CONSTRAINT relationship_destinationuserid_user_id FOREIGN KEY (destinationuserid) REFERENCES user(id);
ALTER TABLE status_update ADD CONSTRAINT status_update_userid_user_id FOREIGN KEY (userid) REFERENCES user(id);
ALTER TABLE status_update_comment ADD CONSTRAINT status_update_comment_userid_user_id FOREIGN KEY (userid) REFERENCES user(id);
ALTER TABLE status_update_comment ADD CONSTRAINT status_update_comment_statusupdateid_status_update_id FOREIGN KEY (statusupdateid) REFERENCES status_update(id);
ALTER TABLE user ADD CONSTRAINT user_accountid_account_id FOREIGN KEY (accountid) REFERENCES account(id);
ALTER TABLE user_employer ADD CONSTRAINT user_employer_userid_user_id FOREIGN KEY (userid) REFERENCES user(id);
ALTER TABLE user_employer ADD CONSTRAINT user_employer_employerid_employer_id FOREIGN KEY (employerid) REFERENCES employer(id);
ALTER TABLE user_interest ADD CONSTRAINT user_interest_userid_user_id FOREIGN KEY (userid) REFERENCES user(id);
ALTER TABLE user_school ADD CONSTRAINT user_school_userid_user_id FOREIGN KEY (userid) REFERENCES user(id);
ALTER TABLE user_school ADD CONSTRAINT user_school_schoolid_school_id FOREIGN KEY (schoolid) REFERENCES school(id);
