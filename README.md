# BulletinBoard

A BulletinBoard application framed by SpringBoot.

Users are able to register accounts through `/accounts` REST apis

Users are able to Create/Update/Fetch/Delete notes through `/notes` REST apis

Data are stored at MySQL database

# Setup needed
1. Install your local MySQL database
2. Create database named `bulletin_board`
3. Create account with name `bulletin_admin` and password as `bulletin_admin`

# Major Contributions
1. [Implement User functionalities](https://github.com/JialinShi/bulletin-board/pull/1) 

# Curl Tests
**Create user**
```
curl -X POST 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--data '{
    "id":1,
    "username": "Aviva",
    "password": 123,
    "email": "aviva@gmail.com"
}'
```

**Get user**
```
curl -X GET 'http://localhost:8080/api/users/1' \
--header 'Accept: application/json'
```

**Update user**
```
curl -X PUT 'http://localhost:8080/api/users/1' \
-H 'Content-Type: application/json' \
-d '{
    "username": "updateduser",
    "password": "updatedpassword",
    "email": "updateduser@example.com"
}'

```
**Delete user**
```
curl -X DELETE 'http://localhost:8080/api/users/1'
```
