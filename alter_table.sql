
ALTER TABLE user RENAME COLUMN name TO username;

ALTER TABLE user ADD COLUMN first_name VARCHAR(200) DEFAULT "test";

ALTER TABLE user ADD COLUMN last_name VARCHAR(200) DEFAULT "test";

ALTER TABLE user DROP COLUMN phone_number;

ALTER TABLE recipe
    ADD COLUMN like_count INT UNSIGNED DEFAULT 0,
    ADD COLUMN comment_count INT UNSIGNED DEFAULT 0;

ALTER TABLE recipe ADD COLUMN save_count INT UNSIGNED DEFAULT 0;

ALTER TABLE user DROP FOREIGN KEY user_ibfk_1;

ALTER TABLE fridge_ingredient DROP FOREIGN KEY fridge_ingredient_ibfk_1;

ALTER TABLE fridge_ingredient CHANGE fridge_id user_id BIGINT UNSIGNED NOT NULL;

ALTER TABLE fridge_ingredient ADD CONSTRAINT fk_fridge_ingredient_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;

ALTER TABLE user drop column fridge_id;

DROP TABLE fridge;

ALTER TABLE ingredient ADD COLUMN image_url VARCHAR(255);

INSERT INTO user (username, email, password, created_at, updated_at, first_name, last_name)
VALUES
    ('janedoe', 'jane.doe@example.com', 'securepass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Jane', 'Doe'),
    ('alexsmith', 'alex.smith@example.com', 'alexpass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Alex', 'Smith');

INSERT INTO fridge_ingredient (user_id, ingredient_id)
VALUES
    (1, 3),
    (2, 2),
    (3, 4),
    (3, 5);
INSERT INTO fridge_ingredient (user_id, ingredient_id)
VALUES
    (4, 3),
    (2, 2),
    (3, 4),
    (3, 5);
