example request cycle:

- POST to `register` endpoint:
```
POST http://localhost:8080/auth-api/register
Content-Type: application/json

{
  "username": "test",
  "password": "123"
}
```
- POST to `login` endpoint:
```
POST http://localhost:8080/auth-api/login
Content-Type: application/json

{
  "username": "test",
  "password": "123"
}
Response: eyJhbGciOiJIUzI1... <JWT Token>
```
- Use this JWT Token to authorize in api:
```
GET http://localhost:8081/recipe-api
Authorization: <JWT Token>
Response:
[
  {
    "id": 1,
    "name": "deneme",
    "description": "deneme",
    "createdAt": null
  }
]
```