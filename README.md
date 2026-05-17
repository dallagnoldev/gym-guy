# 🏋️‍♂️ GymGuy API

**GymGuy** is a RESTful API for workout management and exercises routines, powered by **Spring Boot**. 

## 🚀 Technologies
Build with:
* **Java 21**
* **Spring Boot 4.06**
* **Spring Security** (JWT Auth and Access Control)
* **Spring Data JPA**
* **PostgreSQL**
* **Flyway** (Database Migrations)
* **Lombok**
* **Docker & Docker Compose**
* **Swagger (OpenAPI)** (API Docs)

## 🔐 Main Features
- **JWT Authentication:** Safe login with **Token** generation.
- **RBAC (Role Based Access Control):** User -> (`ROLE_USER`) and Admin -> (`ROLE_ADMIN`).
- **Security:** Common users only can see, update or delete their own data.
- **Workout Management:** Create, Read, Update and Delete your workouts.
- **Subscription Plans:** FREE, PREMIUM.

## 📌 API Endpoints

### 1. Authentication Controller (Public):
  Register
   ```bash
   POST - /api/v1/auth/register
   ```
   ```json
   {
     "firstName": "name",
     "lastName": "lastName",
     "email": "email@gmail.com",
     "password": "Password123@"
     "phoneNumber": "00000000",
     "birthDate": "1900-01-01",
     "sex": "M",
     "height": 0.1,
     "weight": 0.1
   }
   ```

  Login
  ```bash
  POST - /api/v1/auth/login
  ```
  ```json
  {
    "email": "email@gmail.com",
    "password": "Password123@"
  }
  ```
  *Payload:*
  ```json
  {
  "token": "string",
  "expiresIn": 0
  }
  ```

### 2. Workout Controller (Authenticated):
  ```bash
  POST - /api/v1/users/{userId}/workouts - Pageable
  ```
  ```json
  {
  "name": "string",
  "description": "string"
  }
  ```
  ```bash
  GET - /api/v1/users/{userId}/workouts
  ```
  ```json
  {
      "workoutId": 0,
      "name": "string",
      "description": "string",
      "exercises": [
        {
          "exerciseId": 0,
          "exerciseName": "string",
          "weight": 0,
          "reps": 0,
          "sets": 0,
          "position": 0,
          "createdAt": "2026-05-12T19:19:29.938Z",
          "updatedAt": "2026-05-12T19:19:29.938Z"
        }
      ],
      "createdAt": "2026-05-12T19:19:29.938Z",
      "updatedAt": "2026-05-12T19:19:29.938Z"
    }
  ```

### 3. Exercise Controller (Authenticated)
  ```bash
  GET - /api/v1/users/exercises - Pageable
  ```
  ```json
   {
      "exerciseId": 0,
      "name": "string",
      "muscularGroup": "string",
      "createdAt": "2026-05-12T20:07:20.207Z",
      "updatedAt": "2026-05-12T20:07:20.207Z"
    }
  ```

### 4. Workout Exercise Controller (Authenticated)
  ```bash
  POST - /api/v1/workouts/{workoutId}/exercises/{exerciseId}
  ```
  ```json
  {
    "weight": 0,
    "reps": 0,
    "sets": 0,
    "position": 0
  }
  ```

## 🔗 Complete Documentation:
Access the API Docs at Swagger UI (Require running server) :
http://localhost:8080/swagger-ui/index.html


## 🛠️ Requirements
* Java Development Kit(JDK)
* Docker Desktop or Docker terminal.
* Maven

### Step by step:
1. **Clone the repository:**
   ```bash
   git clone https://github.com/dallagnoldev/gym-guy.git
   cd gymguy
   ```

2. **Docker Compose:**
  ```bash
  docker compose up
  ```
3. **Run the application**:
  ```bash
  mvn spring-boot:run
  ```
