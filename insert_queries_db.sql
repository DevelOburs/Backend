INSERT INTO ingredient (name) VALUES
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
INSERT INTO recipe (id, name) VALUES
                                  (70553, 'Banana Spice Cookies'),
                                  (191583, 'Company Cloud Biscuits'),
                                  (164504, 'Ultimate Chocolate Chocolate Chip Cookies'),
                                  (70729, 'Shrimp Carlos'),
                                  (239536, 'Tangy Tarragon Tuna Salad'),
                                  (61783, 'Crispy Chicken Supreme');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
                                                                       -- Banana Spice Cookies
                                                                       (70553, 5, '1 cup'),           -- flour
                                                                       (70553, 11, '1/2 cup'),        -- sugar
                                                                       (70553, 33, '1 tsp'),          -- cinnamon
                                                                       (70553, 30, '1/2 tsp'),        -- ground cinnamon
                                                                       (70553, 39, '1/4 tsp'),        -- salt
                                                                       (70553, 42, '1/2 cup'),        -- unsalted butter
                                                                       (70553, 8, '1/2 cup'),         -- sweetened coconut
                                                                       (70553, 50, '2 eggs'),         -- eggs
                                                                       (70553, 48, '1 tsp'),          -- vanilla extract
                                                                       (70553, 29, '1/2 tsp'),        -- baking soda

                                                                       -- Company Cloud Biscuits
                                                                       (191583, 5, '2 cups'),         -- all-purpose flour
                                                                       (191583, 6, '1/2 cup'),        -- heavy whipping cream
                                                                       (191583, 30, '2 tsp'),         -- baking powder
                                                                       (191583, 48, '1/4 cup'),       -- unsalted butter
                                                                       (191583, 16, '1 tbsp'),        -- honey
                                                                       (191583, 31, '1/2 cup'),       -- milk
                                                                       (191583, 44, '1/2 tsp'),       -- garlic powder

                                                                       -- Ultimate Chocolate Chocolate Chip Cookies
                                                                       (164504, 5, '1 1/2 cups'),     -- all-purpose flour
                                                                       (164504, 11, '1 cup'),         -- sugar
                                                                       (164504, 14, '2 tsp'),         -- vanilla extract
                                                                       (164504, 39, '1/2 tsp'),       -- salt
                                                                       (164504, 8, '1 cup'),          -- sweetened coconut
                                                                       (164504, 3, '1/2 cup'),        -- ground cinnamon
                                                                       (164504, 24, '2 cups'),        -- chocolate chips
                                                                       (164504, 42, '1 cup'),         -- unsalted butter
                                                                       (164504, 50, '2 eggs'),        -- eggs
                                                                       (164504, 29, '1 tsp'),         -- baking soda

                                                                       -- Shrimp Carlos
                                                                       (70729, 68, '1 lb'),           -- shrimp
                                                                       (70729, 5, '1/2 cup'),         -- all-purpose flour
                                                                       (70729, 27, '2 tbsp'),         -- olive oil
                                                                       (70729, 6, '1/4 cup'),         -- heavy whipping cream
                                                                       (70729, 35, '1 tsp'),          -- paprika
                                                                       (70729, 24, '1/4 cup'),        -- fresh parsley
                                                                       (70729, 16, '1 tbsp'),         -- honey
                                                                       (70729, 20, '1/4 cup'),        -- garlic cloves
                                                                       (70729, 51, '1 tsp'),          -- cumin

                                                                       -- Tangy Tarragon Tuna Salad
                                                                       (239536, 18, '1 can'),          -- solid white tuna
                                                                       (239536, 16, '1 tbsp'),        -- honey
                                                                       (239536, 1, '2 tbsp'),         -- lemon juice
                                                                       (239536, 14, '1 tbsp'),        -- vinegar
                                                                       (239536, 49, '1/4 tsp'),       -- ground pepper
                                                                       (239536, 20, '1 clove'),       -- garlic
                                                                       (239536, 3, '1/2 tsp'),        -- ground cinnamon
                                                                       (239536, 4, '1 tbsp'),         -- dill
                                                                       (239536, 11, '1 tbsp'),        -- sugar
                                                                       (239536, 48, '1 tsp'),         -- vanilla extract

                                                                       -- Crispy Chicken Supreme
                                                                       (61783, 4, '1 lb'),            -- frying chickens
                                                                       (61783, 9, '1 cup'),           -- sweetened coconut
                                                                       (61783, 29, '2 tsp'),          -- baking soda
                                                                       (61783, 42, '2 tbsp'),         -- unsalted butter
                                                                       (61783, 13, '1/2 cup'),        -- broth
                                                                       (61783, 37, '1/4 cup'),        -- lemon juice
                                                                       (61783, 34, '1/4 cup'),        -- green onions
                                                                       (61783, 12, '1/2 tsp'),        -- poultry seasoning
                                                                       (61783, 46, '1 tsp'),          -- garlic powder
                                                                       (61783, 1, '1 tbsp');          -- all-purpose flour


-- Insert data into the fridge table to create unique fridge IDs
INSERT INTO fridge VALUES (); -- Creates one fridge with auto-incremented ID 1

-- Insert data into the fridge_ingredient table to add ingredients to the fridge
INSERT INTO fridge_ingredient (fridge_id, ingredient_id) VALUES
                                                             (1, 1),  -- Fridge 1 contains Flour
                                                             (1, 4),  -- Fridge 1 contains Butter
                                                             (1, 5);  -- Fridge 1 contains Eggs

-- Insert data into the users table with a fridge ID reference
INSERT INTO user (fridge_id, name, email, password, first_name, last_name)
VALUES (1, 'John Doe', 'johndoe@example.com', 'password123', 'John', 'Doe');
INSERT INTO allergen (name) VALUES
                                ('Peanuts'),
                                ('Shellfish'),
                                ('Gluten'),
                                ('Dairy'),
                                ('Eggs'),
                                ('Soy'),
                                ('Wheat'),
                                ('Fish'),
                                ('Tree Nuts'),
                                ('Sesame');
-- Insert data into user_allergen to specify any allergens for the user
INSERT INTO user_allergen (user_id, allergen_id) VALUES
    (1, 1);  -- User John Doe is allergic to Salt

-- Insert a comment in recipe_comments for a recipe
INSERT INTO recipe_comment (recipe_id, user_id, comment) VALUES
    (61783, 1, 'These cookies are amazing!');

-- Insert data into recipe_likes to track recipe likes
INSERT INTO recipe_like (recipe_id, user_id, liked) VALUES
    (61783, 1, TRUE);  -- John Doe likes the Chocolate Chip Cookies recipe

-- Insert data into user_recipes to assign a recipe to a user
INSERT INTO user_saved_recipe (user_id, recipe_id) VALUES
    (1, 61783);  -- John Doe created the Banana Bread recipe

