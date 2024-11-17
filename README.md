# Backend

Dataset:
https://www.kaggle.com/datasets/irkaal/foodcom-recipes-and-reviews/data

intellijide ultimate versiyonu okul emaili ile lisanslayıp kurmanız önerilir

to run database:
in root folder, run:
docker-compose up

to delete old db version adn install new:
#### docker compose down
// You should delete volume at this point, you can do it on docker desktop or temrinal
#### docker volume list 
// there should be: backend_fridge-db-data
#### docker volume rm backend_fridge-db-data

docker-compose up
