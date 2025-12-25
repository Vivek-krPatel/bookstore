# Bookstore

Bookstore Website using Spring Boot and Rest api for backend and html, css and JavaScript for frontend.

## About

This repository contains a Bookstore web application built with Spring Boot for the backend (REST API) and a simple frontend using HTML, CSS, and JavaScript. It provides features to list books, add new books, update existing books, and delete books.

## Languages

- Java (backend)
- JavaScript (frontend logic)
- CSS
- HTML

## Features

- RESTful API with Spring Boot
- CRUD operations for books
- Simple responsive frontend (HTML/CSS/JS)
- Example screenshots included

## Setup

Prerequisites:

- Java 11+ or compatible JDK
- Maven
- (Optional) MySQL or H2 database

Steps:

1. Clone the repository:

   git clone https://github.com/Vivek-krPatel/bookstore.git

2. Import the project into your IDE (IntelliJ/Eclipse) as a Maven project.
3. Configure database settings in src/main/resources/application.properties (if using MySQL).
4. Run the application:

   mvn spring-boot:run

5. Open the frontend in your browser (if applicable) or access API endpoints at http://localhost:8080/api

## API (example endpoints)

- GET /api/books - list all books
- GET /api/books/{id} - get a book by id
- POST /api/books - create a new book
- PUT /api/books/{id} - update a book
- DELETE /api/books/{id} - delete a book


## Screenshots

![Screenshot 1](images/screenshot 1.svg)

![Screenshot 2](images/screenshot 2.svg)

![Screenshot 5](images/screenshot 5.svg)

![Screenshot 6](images/screenshot 6.svg)

