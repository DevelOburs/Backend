ALTER TABLE user DROP COLUMN phone_number;

ALTER TABLE user ADD COLUMN first_name VARCHAR(200) DEFAULT "test";

ALTER TABLE user ADD COLUMN last_name VARCHAR(200) DEFAULT "test";