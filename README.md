RESTful Blog Application API

Project Overview

A RESTful Blog Application API developed using Java, Spring Boot, and MySQL. It allows users to register, log in, manage blog posts, and manage comments securely using JWT authentication.

Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Spring Security
- JWT Authentication
- Maven
- Swagger / OpenAPI
- JUnit 5

Features

- User registration and login
- JWT-based authentication
- Create, read, update, and delete blog posts
- Create, read, update, and delete comments
- Author-based access control for post modifications
- Input validation
- Swagger API documentation
- Automated tests

Database Setup

1. Install and start MySQL.
2. Create a database named "blog_db".
3. Configure the database URL, username, password, and JWT secret in "src/main/resources/application.properties".
4. Never commit real passwords or secret keys to GitHub.

Run the Application

On Windows, open a terminal in the project directory and run:

.\mvnw.cmd spring-boot:run

Run Tests

.\mvnw.cmd clean test

API Documentation

After starting the application, open:

http://localhost:8080/swagger-ui/index.html

Main API Endpoints

Method| Endpoint| Purpose
POST| "/api/auth/register"| Register a user
POST| "/api/auth/login"| Log in
GET| "/api/posts"| Get all posts
POST| "/api/posts"| Create a post
GET| "/api/posts/{id}"| Get one post
PUT| "/api/posts/{id}"| Update a post
DELETE| "/api/posts/{id}"| Delete a post
POST| "/api/comments"| Create a comment

Additional comment endpoints are available in Swagger.

Testing

Automated tests cover application startup, health endpoint behavior, and validation of post, comment, and login request data.

Project Submission

Upload the source code and this README to GitHub, then submit the repository URL using the assigned task submission form.