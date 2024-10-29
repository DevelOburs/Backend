-- Ingredient table to store ingredient information
CREATE TABLE `ingredient` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `extras` BIGINT
);

-- Recipes table to store recipe information
CREATE TABLE `recipe` (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Recipe ingredients table to store the ingredients for each recipe
CREATE TABLE `recipe_ingredient` (
    `recipe_id` BIGINT UNSIGNED NOT NULL,
    `ingredient_id` BIGINT UNSIGNED NOT NULL,
    `quantity` VARCHAR(50),
    `extras` BIGINT,
    PRIMARY KEY (`recipe_id`, `ingredient_id`)
);

-- Fridge table to store unique fridge IDs
CREATE TABLE `fridge` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
);

-- Fridge ingredients table to store the ingredients present in each fridge
CREATE TABLE `fridge_ingredient` (
    `fridge_id` BIGINT UNSIGNED NOT NULL,
    `ingredient_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`fridge_id`, `ingredient_id`)
);

-- Users table to store user information
CREATE TABLE `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `fridge_id` BIGINT UNSIGNED NULL, -- Allow NULL values here
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(15),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `extras` BIGINT
);

-- User app settings table
CREATE TABLE `user_app_setting` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `extras` BIGINT,
    PRIMARY KEY (`user_id`)
);

-- User allergen table to store user-specific allergens
CREATE TABLE `user_allergen` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `ingredient_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `ingredient_id`)
);

-- User favorite recipes table to store user's favorite recipes
CREATE TABLE `user_favorite_recipe` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `recipe_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `recipe_id`)
);

-- Recipe comments table to store comments made by users on recipes
CREATE TABLE `recipe_comment` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `recipe_id` BIGINT UNSIGNED NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `comment` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Recipe like table to store likes on recipes by users
CREATE TABLE `recipe_like` (
    `recipe_id` BIGINT UNSIGNED NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `liked` BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (`recipe_id`, `user_id`)
);

-- User recipe table to store recipes created by users
CREATE TABLE `user_recipe` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `recipe_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `recipe_id`)
);