# Project: bulletin-board
Author: `Jialin Shi`

Live website: http://ec2-18-222-52-169.us-east-2.compute.amazonaws.com/

## Description

This is a bulletin board application where users are able to 
- Register an account
- Login the account
- Create, Review, Update, and Delete the notes they own

### Tech Stack
| Item | Tech stack |
| ------ | ------ |
| Coding | Java 21 |
| Framework | Spring Boot |
| Endpoint | REST API |
| Database | MySQL |
| Dependency Management  | Maven |
| Authentication | Spring Security, JWT |
| Deployment | AWS EC2, Nginx |
| Unit Test | JUnit, Mockito |

### Architecture 

<img src="https://github.com/user-attachments/assets/6357574f-15e5-455d-9bbe-0e1f5075df3e" width="600" height="400" alt="bulletin-board" />


#### Athentication - [AuthController.java](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/controller/AuthController.java)

This module only has one method login(), which accepts POST requests for user log in.  
Leveraging **AuthenticationManager** to validate user's log in request, if authentication request passed, user details will be read from database and [JwtUtil](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/security/JwtUtil.java) is utilized to generate a JWT token as a prove of this authenticated user, then returned to user session. 
<br>
#### User Operations - [UserController.java](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/controller/UserController.java)

This controller manages [User](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/models/User.java) related operations within the system, providing a RESTful interface for creating, retrieving, updating, and deleting user information.  

Defined under the `/api/users` route, the `UserController` integrates various endpoints:

- **POST `/api/users`**: Accepts user details in the request body to create a new user. The newly created user's password is set to null before being sent back, avoiding sensitive data breach. This operation returns the user details with a `201 CREATED` status to indicate a successful creation.

- **GET `/api/users`**: Retrieves a list of all users in the system, used for for administrative purposes or user management interfaces.

- **GET `/api/users/{id}`**: Fetches a single user by their unique identifier. This is crucial for editing interfaces or user profile views.

- **PUT `/api/users/{id}`**: Updates user information based on the provided ID and user details in the request body. This endpoint is essential for maintaining accurate and up-to-date user information.

- **DELETE `/api/users/{id}`**: Removes a user from the system by their ID. This operation is critical for managing user lifecycles and ensuring data privacy and compliance.

The class leverages Spring's `@Autowired` to inject the [UserService](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/service/UserService.java), which encapsulates all the business logic for user management. 
<br>
#### Note Operations - [NoteController.java](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/controller/NoteController.java)

This controller manages [Note](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/models/Note.java) related operations within the system, providing a RESTful interface for creating, retrieving, updating, and deleting notes. 

Defined under the `/api/notes` route, the `NoteController` integrates various endpoints:

- **POST `/api/notes`**: Accepts note details in the request body to create a new note. The newly created note is associated with the authenticated user and returned in the response.

- **GET `/api/notes/user/{userId}`**: Retrieves a list of all notes created by a specific user, useful for user-specific note management and review interfaces. This endpoint ensures that only the owner can access the note details by validating the owners of the notes with current userId. Returning 403 unauthorized if this request is not successfuly validated.

- **GET `/api/notes/{noteId}`**: Fetches a single note by its unique identifier. This endpoint ensures that only the owner or authorized users can access the note details.

- **PUT `/api/notes/user/{userId}/note/{noteId}`**: Updates a specific note based on the provided user ID and note ID. This operation allows users to modify their notes if they are the rightful owners.

- **DELETE `/api/notes/user/{userId}/note/{noteId}`**: Removes a note from the system by its ID, based on user authentication and ownership. This endpoint is crucial for managing the lifecycle of notes and ensuring data privacy.

The class leverages Spring's `@Autowired` to inject [NoteService](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/service/NoteService.java) and [UserService](https://github.com/JialinShi/bulletin-board/blob/main/src/main/java/com/jialin/BulletinBoard/service/UserService.java), which encapsulates all business logic related to note management. 




## Steps to Deploy Locally
1. Install local MySQL database  
```
brew install mysql
```
   
3. Create database named `bulletin_board`  
```
#login with the root user
mysql -u root -p

# create database
CREATE DATABASE bulletin_board;
```

3. Create account with name `bulletin_admin` and password as `bulletin_admin`  
```
CREATE USER 'bulletin_admin'@'localhost' IDENTIFIED BY 'bulletin_admin';
GRANT ALL PRIVILEGES ON bulletin_board.* TO 'bulletin_admin'@'localhost';
FLUSH PRIVILEGES;
```

4. Clone this repository  
```
git clone https://github.com/JialinShi/bulletin-board.git
```

6. Go to repo folder  
```
cd bulletin-board
```

8. Kick off this application  
```
./mvnw spring-boot:run
```



# Picked PRs
- [Implement User functionalities](https://github.com/JialinShi/bulletin-board/pull/1)
- [Unit test for UserService](https://github.com/JialinShi/bulletin-board/pull/3)
- [Implement Note functionalities](https://github.com/JialinShi/bulletin-board/commit/d521ddce912af857d0d89c111cd7fb7698d86f00)

