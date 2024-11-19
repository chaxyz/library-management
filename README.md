<h1 style="color:blue;">Library Management System - Setup Guide</h1>

This repository contains the implementation of a Library Management System with the following structure:  

- **FE**: Frontend implemented using Vue 3.  
- **BE**: Backend implemented using Spring Boot.  
- **DB**: Database scripts for creating and inserting records into the database.  
- **AMAZON S3** : For file stroage
---

## Prerequisites

Ensure you have the following installed:

- **Node.js** (v16+)
- **npm** or **yarn**
- **Java 17** (or higher)
- **Maven**
- **MySQL Server**
- **Git**

---
<h1 style="color:blue;">BACKEND-ENDPOINT</h1>

## Authentication API Endpoints

| Endpoint         | Method | Description                                                                                                      | Request Example                                                                                                                                 | Response Example                                                                                                                                           |
|-------------------|--------|------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/signup`         | POST   | Registers a new user in the system.                                                                              | ```json { "username": "string", "password": "string", "name": "string" } ```                                                                  | ```json { "id": "string", "username": "string", "name": "string","role":"USER,ADMIN" } ```                                                                                     |
| `/login`          | POST   | Authenticates a user and generates a JWT token for further access.                                               | ```json { "username": "string", "password": "string" } ```                                                                                     | ```json { "access_token": "string", "refresh_token": "string" } ```                                                                                           |
| `/token`          | POST   | Refreshes the access token using a valid refresh token.                                                         | **Header:** `Authorization: Bearer <refresh_token>`                                                                                           | ```json { "access_token": "string" } ```                                                                                                                     |

---

## Book API Endpoints

| Endpoint         | Method | Description                                                                                      | Request Example                                                                                              | Response Example                                                                                                                                                 |
|-------------------|--------|--------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/books`          | POST   | Creates a new book. Supports optional image upload for the book cover.                          | **Request Part:** `bookDetails` (BookDto object), **Request Param:** `file` (image file)                    | ```json { "id": 1, "name": "string", "status": "returned", "path": "string", "category": { "id": 1, "name": "string" } } ```                                     |
| `/books/{id}`     | GET    | Retrieves a book by its ID.                                                                     | **Path Variable:** `id` (Integer)                                                                           | ```json { "id": 1, "name": "string", "status": "returned", "path": "string", "category": { "id": 1, "name": "string" } } ```                                     |
| `/books`          | GET    | Retrieves all books. Can filter books by category ID using query parameters.                    | **Query Param (optional):** `categoryId` (Integer)                                                          | ```json [ { "id": 1, "name": "string", "status": "returned", "path": "string", "category": { "id": 1, "name": "string" } } ] ```                                  |
| `/books/{id}`     | PUT    | Updates an existing book by its ID. Supports optional image update or removal.                  | **Path Variable:** `id` (Integer), **Request Part:** `bookDetails` (BookDto object), **Request Param:** `file` (image file) | ```json { "id": 1, "name": "updatedString", "status": "borrowed", "path": "newString", "category": { "id": 2, "name": "newCategory" } } ```                     |
| `/books/{id}`     | DELETE | Deletes a book by its ID. Removes the associated image file if one exists.                      | **Path Variable:** `id` (Integer)                                                                           | No content returned. HTTP status: `204 No Content`                                                                                                              |

---

## Category API Endpoints

| Endpoint         | Method | Description                                                                                       | Request Example                                                                                         | Response Example                                                                                                        |
|-------------------|--------|---------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| `/categories`     | POST   | Creates a new category.                                                                          | ```json { "name": "string" } ```                                                                       | ```json { "id": 1, "name": "string" } ```                                                                              |
| `/categories/{id}`| GET    | Retrieves a category by its ID.                                                                  | **Path Variable:** `id` (Integer)                                                                      | ```json { "id": 1, "name": "string" } ```                                                                              |
| `/categories`     | GET    | Retrieves all categories.                                                                        | No additional parameters required.                                                                     | ```json [ { "id": 1, "name": "string" }, { "id": 2, "name": "anotherString" } ] ```                                    |
| `/categories/{id}`| PUT    | Updates an existing category by its ID.                                                          | **Path Variable:** `id` (Integer), **Request Body:** ```json { "name": "updatedString" } ```            | ```json { "id": 1, "name": "updatedString" } ```                                                                       |
| `/categories/{id}`| DELETE | Deletes a category by its ID.                                                                    | **Path Variable:** `id` (Integer)                                                                      | No content returned. HTTP status: `204 No Content`                                                                     |

---
<h1 style="color:blue;">Setup Steps</h1>

### 1. Clone the Repository

```bash
git clone https://github.com/chaxyz/library-management.git
cd library-management
```

---

### 2. Database Setup

#### a. Create the Database
1. Open your MySQL client or any database management tool (e.g., MySQL Workbench, phpMyAdmin, or the MySQL CLI).
2. Navigate to the `Library-Management-DB/create` folder in this repository.
3. Execute the following scripts in order:
   - `create-script.sql` - Creates the database schema and table.

#### b. Insert Sample Data
1. Navigate to the `Library-Management-DB/insert` folder in this repository.
2. Execute the `insert-script.sql` script to populate the database with initial sample records.

---

**Note:**  
- Replace `your_database_name` in the scripts with the desired database name if necessary.  
- Ensure that the MySQL server is running before executing the scripts.

---

### 3. Backend Setup (BE)

1. Navigate to the `BE` directory:

   ```bash
   cd Library-Management-BE
   ```

2. Open the `application.properties` file located in `src/main/resources` and update the following properties with your MySQL database details:
   - DB connnection
     
    ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
    ```
   
    - AWS Configuration
      
    ```bash
    cloud.aws.credentials.access-key=your_public_key
    cloud.aws.credentials.secret-key=your_secret_key
    cloud.aws.region.static=your_bucket_region
    application.bucket.name=your_bucket_name
    ```
    
    - CORS or relace `//localhost:5173/` with your `CORS`
      
    ```bash
    cors.allowed.origins=http://localhost:5173/
    ```
    
3. Build the backend project:
   ```bash
   mvn clean install
   ```

4. Start the Spring Boot server:
   ```bash
   mvn spring-boot:run
   ```
5. The backend server will run at:
    ```bash
    http://localhost:8080
    ```
Note:
Ensure that your MySQL database is set up and running before starting the backend server.

---

### 4. Frontend Setup (FE)

1. Navigate to the `FE` directory:
   
   ```bash
   cd Library-Management-FE
   ```
2. Install the dependencies:
   ```bash
   npm install
   # Or, if you are using Yarn:
   yarn install
   ```
3. Start the development server:
   ```bash
   npm run dev
   # Or, if you are using Yarn:
   yarn dev
   ```

4. Access the frontend at:
   ```bash
   http://localhost:5173
   ```

---

### 5. Usage

- Open your browser and go to:
  `http://localhost:5173`
  to access the frontend.

- Ensure the backend server is running on:
  `http://localhost:8080`
  for API access.
