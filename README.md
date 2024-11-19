# Library Management System - Setup Guide

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

## Setup Steps

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
