# Employee Database Management System

## Overview
This project is a **Spring Boot-based Employee Database Management System** that provides CRUD operations for managing employee records. It allows adding, updating, viewing, and deleting employee information while ensuring proper validation and exception handling.

## Features
- **Add Employee:** Add a new employee with automatic ID generation.
- **Update Employee:** Change an employee's manager or other details.
- **View Employee:** Retrieve employees based on experience or manager ID.
- **Delete Employee:** Remove employees with validation checks.
- **Global Exception Handling:** Handles validation errors gracefully.

## Technologies Used
- **Spring Boot** - Backend framework
- **Spring Data JPA** - ORM for database interaction
- **MySQL** - Database
- **Maven** - Build tool
- **Java 11+** - Programming language

## API Endpoints
### Add Employee
```
POST /AddEmployee
Request Body: Employee JSON Object
Response: Success/Failure Message
```

### Update Employee
```
PUT /update
Request Body: Employee ID & Updated Fields
Response: Success/Failure Message
```

### View Employees
```
GET /ViewEmployee
Query Params: year-of-experience (optional), managerId (optional)
Response: List of Employees
```

### Delete Employee
```
DELETE /remove
Query Params: employeeId
Response: Success/Failure Message
```

## Installation & Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo.git
   ```
2. Navigate to the project directory:
   ```sh
   cd EmployeeDb
   ```
3. Configure **application.properties** with your database credentials.
4. Build the project:
   ```sh
   mvn clean install
   ```
5. Run the application:
   ```sh
   mvn spring-boot:run
   ```


