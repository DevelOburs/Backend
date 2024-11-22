-- Ingredient table to store ingredient information
CREATE TABLE ingredient (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50)
);

-- Fridge table to store unique fridge IDs
CREATE TABLE fridge (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
);

-- Users table to store user information
CREATE TABLE user (
     id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
     fridge_id BIGINT UNSIGNED NULL, -- Foreign key to fridge table
     name VARCHAR(100) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     first_name VARCHAR(200) DEFAULT 'test',
     last_name VARCHAR(200) DEFAULT 'test',
     FOREIGN KEY (fridge_id) REFERENCES fridge(id) ON DELETE SET NULL
);
-- Recipes table to store recipe information
CREATE TABLE recipe (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT UNSIGNED, -- Foreign key to the user table
    image_url VARCHAR(255), -- Column to store the image URL
    like_count INT UNSIGNED DEFAULT 0,
    comment_count INT UNSIGNED DEFAULT 0,
    save_count INT UNSIGNED DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
);

-- Recipe ingredients table to store the ingredients for each recipe
CREATE TABLE recipe_ingredient (
    recipe_id BIGINT UNSIGNED NOT NULL,
    ingredient_id BIGINT UNSIGNED NOT NULL,
    quantity VARCHAR(50),
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

-- Fridge ingredients table to store the ingredients present in each fridge
CREATE TABLE fridge_ingredient (
    fridge_id BIGINT UNSIGNED NOT NULL,
    ingredient_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (fridge_id, ingredient_id),
    FOREIGN KEY (fridge_id) REFERENCES fridge(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

-- User app settings table
CREATE TABLE user_app_setting (
    user_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Allergen table to store allergen information
CREATE TABLE allergen (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);

-- User allergen table to store user-specific allergens
CREATE TABLE user_allergen (
    user_id BIGINT UNSIGNED NOT NULL,
    allergen_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, allergen_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (allergen_id) REFERENCES allergen(id) ON DELETE CASCADE
);

-- Allergen-ingredient mapping table
CREATE TABLE allergen_ingredient (
    ingredient_id BIGINT UNSIGNED NOT NULL,
    allergen_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (ingredient_id, allergen_id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE,
    FOREIGN KEY (allergen_id) REFERENCES allergen(id) ON DELETE CASCADE
);

-- User favorite recipes table to store user's favorite recipes
CREATE TABLE user_saved_recipe (
    user_id BIGINT UNSIGNED NOT NULL,
    recipe_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, recipe_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE
);

-- Recipe comments table to store comments made by users on recipes
CREATE TABLE recipe_comment (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Recipe like table to store likes on recipes by users
CREATE TABLE recipe_like (
    recipe_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    liked BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (recipe_id, user_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);