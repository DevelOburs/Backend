

INSERT INTO `ingredient` (`name`) VALUES
    ('baking soda'),
    ('lemon'),
    ('lime juice'),
    ('frying chickens'),
    ('flour'),
    ('heavy whipping cream'),
    ('monterey jack pepper cheese'),
    ('unsalted butter'),
    ('sweetened coconut'),
    ('fresh nutmeg'),
    ('sugar'),
    ('broth'),
    ('lean ground beef'),
    ('all-purpose flour'),
    ('walnuts'),
    ('onions'),
    ('habaneros'),
    ('solid white tuna'),
    ('nutmeg'),
    ('ricotta cheese'),
    ('garlic cloves'),
    ('lime wedge'),
    ('raisins'),
    ('cream cheese'),
    ('honey'),
    ('distilled vinegar'),
    ('nonfat milk'),
    ('tomato puree'),
    ('potato'),
    ('poultry seasoning'),
    ('celery'),
    ('penne pasta'),
    ('cinnamon'),
    ('hard-boiled eggs'),
    ('zucchini'),
    ('red onion'),
    ('mayonnaise'),
    ('stewed tomatoes'),
    ('capers'),
    ('eggs'),
    ('gumbo file'),
    ('fresh mushrooms'),
    ('bay leaf'),
    ('Tabasco sauce'),
    ('green pepper'),
    ('ground cinnamon'),
    ('salt'),
    ('sour cream'),
    ('dill pickles'),
    ('fresh rosemary'),
    ('light corn syrup'),
    ('parmesan cheese'),
    ('ancho chili'),
    ('garlic'),
    ('pepper'),
    ('ground cloves'),
    ('onion'),
    ('yellow onion'),
    ('garlic powder'),
    ('vanilla extract'),
    ('frozen limeade concentrate'),
    ('andouille sausage'),
    ('tarragon'),
    ('red onions'),
    ('plain yogurt'),
    ('vanilla'),
    ('pecans'),
    ('chicken broth'),
    ('brown sugar'),
    ('dill'),
    ('fresh parsley'),
    ('seltzer water'),
    ('margarine'),
    ('diced tomatoes'),
    ('fresh basil'),
    ('banana'),
    ('egg'),
    ('chili powder'),
    ('green onion'),
    ('milk'),
    ('mozzarella cheese'),
    ('boiling water'),
    ('bacon'),
    ('fresh parmesan cheese'),
    ('celery rib'),
    ('cumin'),
    ('butter'),
    ('garlic clove'),
    ('cake flour'),
    ('onion powder'),
    ('granulated sugar'),
    ('shortening'),
    ('parsley flakes'),
    ('baking powder'),
    ('olive oil'),
    ('ground black pepper'),
    ('vanilla ice cream'),
    ('tomato sauce'),
    ('paprika'),
    ('angel hair pasta'),
    ('shrimp'),
    ('bay leaves');


INSERT INTO `recipe_ingredient` (`recipe_id`, `ingredient_id`, `quantity`) VALUES
    (70553, 92, '1⁄2'), (70553, 69, '1'), (70553, 40, '2'), (70553, 76, '1'), 
    (70553, 5, '2'), (70553, 94, '2'), (70553, 46, '1⁄2'), (70553, 56, '1⁄4'), 
    (70553, 1, '1⁄4'), (70553, 47, '1⁄4'), (70553, 15, '1⁄2'), (70553, 23, '1⁄2'),
    
    (191583, 5, '2'), (191583, 92, '1⁄2'), (191583, 80, '2⁄3'), (191583, 77, '1'),
    (191583, 94, '4'), (191583, 47, '1⁄2'), (191583, 11, '1'),

    (164504, 14, '4'), (164504, 1, '2 2⁄3'), (164504, 47, '1'), (164504, 87, '1'),
    (164504, 73, '1'), (164504, 69, '1'), (164504, 91, '1⁄2'), (164504, 60, '1'), 
    (164504, 40, '3'),

    (70729, 83, '6'), (70729, 101, '12'), (70729, 64, '12'), (70729, 7, '12'), 
    (70729, 59, '1'), (70729, 44, NULL),

    (239536, 18, '2'), (239536, 34, '2'), (239536, 15, '1⁄4'), (239536, 31, '1⁄4'),
    (239536, 49, '1⁄4'), (239536, 39, '1⁄4'), (239536, 36, '3'), (239536, 65, '1'), 
    (239536, 48, '1⁄4'), (239536, 37, '1'), (239536, 70, '1⁄2'), (239536, 63, NULL),

    (61783, 99, '4'), (61783, 77, '1'), (61783, 80, '1'), (61783, 5, '3⁄4'), 
    (61783, 47, '3⁄4'), (61783, 55, '1 1⁄2'), (61783, 30, '1⁄4'), (61783, 4, '1'), 
    (61783, 73, '2'), (61783, 87, '3');


INSERT INTO `recipe` (`id`, `name`) VALUES
    (70553, 'Banana Spice Cookies'),
    (191583, 'Company Cloud Biscuits'),
    (164504, 'Ultimate Chocolate Chocolate Chip Cookies'),
    (70729, 'Shrimp Carlos'),
    (239536, 'Tangy Tarragon Tuna Salad'),
    (61783, 'Crispy Chicken Supreme');



-- Insert data into the fridge table to create unique fridge IDs
INSERT INTO `fridge` VALUES (); -- Creates one fridge with auto-incremented ID 1

-- Insert data into the fridge_ingredient table to add ingredients to the fridge
INSERT INTO `fridge_ingredient` (`fridge_id`, `ingredient_id`) VALUES 
    (1, 1),  -- Fridge 1 contains Flour
    (1, 4),  -- Fridge 1 contains Butter
    (1, 5);  -- Fridge 1 contains Eggs

-- Insert data into the users table with a fridge ID reference
INSERT INTO `user` (`fridge_id`, `name`, `email`, `password`, `phone_number`) VALUES
    (1, 'John Doe', 'johndoe@example.com', 'password123', '1234567890');

-- Insert data into user_app_settings for the user
INSERT INTO `user_app_setting` (`user_id`, `extras`) VALUES
    (1, NULL);

-- Insert data into user_allergen to specify any allergens for the user
INSERT INTO `user_allergen` (`user_id`, `ingredient_id`) VALUES 
    (1, 3);  -- User John Doe is allergic to Salt

-- Insert data into user_favorite_recipes to add a favorite recipe for the user
INSERT INTO `user_favorite_recipe` (`user_id`, `recipe_id`) VALUES
    (1, 1);  -- John Doe's favorite recipe is Chocolate Chip Cookies

-- Insert a comment in recipe_comments for a recipe
INSERT INTO `recipe_comment` (`recipe_id`, `user_id`, `comment`) VALUES
    (1, 1, 'These cookies are amazing!');

-- Insert data into recipe_likes to track recipe likes
INSERT INTO `recipe_like` (`recipe_id`, `user_id`, `liked`) VALUES
    (1, 1, TRUE);  -- John Doe likes the Chocolate Chip Cookies recipe

-- Insert data into user_recipes to assign a recipe to a user
INSERT INTO `user_recipe` (`user_id`, `recipe_id`) VALUES
    (1, 2);  -- John Doe created the Banana Bread recipe
