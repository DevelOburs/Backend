# Backend

This is the backend side of the _Fridgify_ app. There are
3 microservices:
- auth-api: Handles authorization and creation of JWT.
- user-api: Handles register/login and other user related operations.
- recipe-api: Handles the core logic related to recipes and ingredients.

Microservices are communicating with Spring Eureka. Gateway
then listens the Eureka server and routes requests to appropriate
microservice and related endpoints.

In order to run api in local, you should start the mysql database:
```
docker compose up
```

### Swagger
- recipe: http://localhost:8081/swagger-ui/index.html#/
- auth: http://localhost:8083/swagger-ui/index.html#/
- user: http://localhost:8082/swagger-ui/index.html#/

localhost urls can be replaced with production urls.

### Deployment
* aws
  - gateway and eureka services running in ec2 instance.
  - http://ec2-51-21-135-63.eu-north-1.compute.amazonaws.com:8080

* render
  - auth, user and recipe apis running on render platform. Sleeps if no request sent recently. 
  - https://recipe-api-yikp.onrender.com
  - https://auth-api-i2v8.onrender.com
  - https://user-api-ml66.onrender.com

### Testing
Tests can be executed with after running the test database:
```
docker compose -f docker-compose-test.yml up
./gradlew test
```
