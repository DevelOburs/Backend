INSERT INTO ingredient (name, category) VALUES
                                            ('baking soda', 'baking'),
                                            ('lemon', 'fruits'),
                                            ('lime juice', 'condiments'),
                                            ('frying chickens', 'meat'),
                                            ('flour', 'baking'),
                                            ('heavy whipping cream', 'dairy'),
                                            ('monterey jack pepper cheese', 'dairy'),
                                            ('unsalted butter', 'dairy'),
                                            ('sweetened coconut', 'baking'),
                                            ('fresh nutmeg', 'spices'),
                                            ('sugar', 'baking'),
                                            ('broth', 'liquids'),
                                            ('lean ground beef', 'meat'),
                                            ('all-purpose flour', 'baking'),
                                            ('walnuts', 'nuts'),
                                            ('onions', 'vegetables'),
                                            ('habaneros', 'vegetables'),
                                            ('solid white tuna', 'seafood'),
                                            ('nutmeg', 'spices'),
                                            ('ricotta cheese', 'dairy'),
                                            ('garlic cloves', 'vegetables'),
                                            ('lime wedge', 'fruits'),
                                            ('raisins', 'dried-fruits'),
                                            ('cream cheese', 'dairy'),
                                            ('honey', 'sweeteners'),
                                            ('distilled vinegar', 'condiments'),
                                            ('nonfat milk', 'dairy'),
                                            ('tomato puree', 'sauces'),
                                            ('potato', 'vegetables'),
                                            ('poultry seasoning', 'spices'),
                                            ('celery', 'vegetables'),
                                            ('penne pasta', 'grains'),
                                            ('cinnamon', 'spices'),
                                            ('hard-boiled eggs', 'protein'),
                                            ('zucchini', 'vegetables'),
                                            ('red onion', 'vegetables'),
                                            ('mayonnaise', 'condiments'),
                                            ('stewed tomatoes', 'vegetables'),
                                            ('capers', 'condiments'),
                                            ('eggs', 'protein'),
                                            ('gumbo file', 'spices'),
                                            ('fresh mushrooms', 'vegetables'),
                                            ('bay leaf', 'spices'),
                                            ('Tabasco sauce', 'condiments'),
                                            ('green pepper', 'vegetables'),
                                            ('ground cinnamon', 'spices'),
                                            ('salt', 'spices'),
                                            ('sour cream', 'dairy'),
                                            ('dill pickles', 'pickled'),
                                            ('fresh rosemary', 'herbs'),
                                            ('light corn syrup', 'sweeteners'),
                                            ('parmesan cheese', 'dairy'),
                                            ('ancho chili', 'spices'),
                                            ('garlic', 'vegetables'),
                                            ('pepper', 'spices'),
                                            ('ground cloves', 'spices'),
                                            ('onion', 'vegetables'),
                                            ('yellow onion', 'vegetables'),
                                            ('garlic powder', 'spices'),
                                            ('vanilla extract', 'baking'),
                                            ('frozen limeade concentrate', 'beverages'),
                                            ('andouille sausage', 'meat'),
                                            ('tarragon', 'herbs'),
                                            ('red onions', 'vegetables'),
                                            ('plain yogurt', 'dairy'),
                                            ('vanilla', 'baking'),
                                            ('pecans', 'nuts'),
                                            ('chicken broth', 'liquids'),
                                            ('brown sugar', 'baking'),
                                            ('dill', 'herbs'),
                                            ('fresh parsley', 'herbs'),
                                            ('seltzer water', 'beverages'),
                                            ('margarine', 'dairy'),
                                            ('diced tomatoes', 'vegetables'),
                                            ('fresh basil', 'herbs'),
                                            ('banana', 'fruits'),
                                            ('egg', 'protein'),
                                            ('chili powder', 'spices'),
                                            ('green onion', 'vegetables'),
                                            ('milk', 'dairy'),
                                            ('mozzarella cheese', 'dairy'),
                                            ('boiling water', 'liquids'),
                                            ('bacon', 'meat'),
                                            ('fresh parmesan cheese', 'dairy'),
                                            ('celery rib', 'vegetables'),
                                            ('cumin', 'spices'),
                                            ('butter', 'dairy'),
                                            ('garlic clove', 'vegetables'),
                                            ('cake flour', 'baking'),
                                            ('onion powder', 'spices'),
                                            ('granulated sugar', 'baking'),
                                            ('shortening', 'baking'),
                                            ('parsley flakes', 'herbs'),
                                            ('baking powder', 'baking'),
                                            ('olive oil', 'oils'),
                                            ('ground black pepper', 'spices'),
                                            ('vanilla ice cream', 'desserts'),
                                            ('tomato sauce', 'sauces'),
                                            ('paprika', 'spices'),
                                            ('angel hair pasta', 'grains'),
                                            ('shrimp', 'seafood'),
                                            ('bay leaves', 'spices');

-- categories = [ "fruits", "baking",  "condiments", "meat", "dairy", "spices", "liquids", "nuts", "vegetables", "dried-fruits", "sweeteners", "sauces",  "grains", "protein", "pickled",  "herbs",  "beverages", "seafood", "oils",  "desserts"] --

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

