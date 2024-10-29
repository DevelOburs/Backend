ALTER TABLE recipes RENAME TO recipe;

ALTER TABLE users RENAME TO user;

ALTER TABLE user_app_settings RENAME TO user_app_setting;

ALTER TABLE user_favorite_recipes RENAME TO user_favorite_recipe;

ALTER TABLE recipe_comments RENAME TO recipe_comment;

ALTER TABLE recipe_likes RENAME TO recipe_like;

ALTER TABLE user_recipes RENAME TO user_recipe;

ALTER TABLE recipe ADD COLUMN description TEXT;

ALTER TABLE recipe ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;






