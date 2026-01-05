# HenriTrip Backend

Java Spring Boot Backend Development for a Fullstack Web Project with an Application-Oriented Focus, Close to Professional Contexts.

## Description

Implementation of REST APIs to manage travel guides. Data modeling and SQL management, applying development best practices for maintainability and scalability. Easily integrable with an Angular frontend.

---

## Table of Contents

- [Technologies](#technologies)
- [Features](#features)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Build and Run](#build-and-run)
- [Running with Docker](#running-with-docker)
- [Database Migrations](#database-migrations)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

---

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security 6 (JWT)
- PostgreSQL
- Flyway
- Lombok
- Docker & Docker Compose

---

## Features

- User management with roles (`USER`, `ADMIN`)
- JWT-based authentication
- CRUD operations for: User, Guide, Activity, Audience, Season, Mobility
- Database migrations via Flyway
- Initial data seeding (users, guides, activities)
- Role-based access control
- Integration-ready for Angular frontend

---

## Getting Started

### Prerequisites

- Java 17+
- Maven (ou Gradle)
- PostgreSQL database
- Docker (optionnel, pour setup rapide)

### Clone the repository

```bash
git clone https://github.com/yourusername/henri-trip-backend.git
cd henri-trip-backend
```

---

## Configuration

1. Copiez `.env.example` en `.env` et adaptez les variables (DB, JWT, etc.) :

```bash
cp .env.example .env
```

2. Vérifiez/complétez `src/main/resources/application.yml` selon votre environnement.

---

## Build and Run

### Local (Maven)

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Running with Docker

Build the Docker image :

```bash
docker build -t henri-trip-backend .
```

Run with Docker Compose :

```bash
docker-compose up
```

Stop with Docker Compose :

```bash
docker-compose down
```

Stop and delete volumes with Docker Compose :

```bash
docker-compose down -v
```

Build and create containers in detached mode with Docker Compose :

```bash
docker-compose up --build -d
```

---

## Database Migrations

Les migrations Flyway sont appliquées automatiquement au démarrage.

Pour lancer manuellement :

```bash
./mvnw flyway:migrate
```

---

## API Endpoints

### Authentication

- **POST** `/api/auth/login` – Authenticate and return JWT token

---

### Users

- **GET** `/api/users` _(Admin only)_ – List all users
- **GET** `/api/users/{userId}` _(Admin only)_ – Get user by ID
- **POST** `/api/users` _(Admin only)_ – Create user
- **PUT** `/api/users/{userId}` _(Admin only)_ – Update user
- **DELETE** `/api/users/{userId}` _(Admin only)_ – Delete user
- **GET** `/api/users/me` _(User/Admin)_ – Get current logged-in user

---

### Guides

- **GET** `/api/guides/visible/{userId}` _(User/Admin)_ – List guides visible to a user
- **GET** `/api/guides/{guideId}/forUser/{userId}` _(User/Admin)_ – Get guide details for a specific user
- **GET** `/api/admin/guides` _(Admin only)_ – List all guides
- **GET** `/api/admin/guides/{guideId}` _(Admin only)_ – Get guide by ID
- **POST** `/api/admin/guides?creatorUserId={creatorUserId}` _(Admin only)_ – Create a guide
- **PUT** `/api/admin/guides/{guideId}` _(Admin only)_ – Update a guide
- **DELETE** `/api/admin/guides/{guideId}` _(Admin only)_ – Delete a guide

---

### Activities

- **GET** `/api/guides/{guideId}/activities?dayNumber={dayNumber}` _(User/Admin)_ – List activities for a guide (optionally filtered by day)
- **POST** `/api/admin/guides/{guideId}/activities` _(Admin only)_ – Create an activity for a guide
- **PUT** `/api/admin/guides/{guideId}/activities/{activityId}` _(Admin only)_ – Update an activity
- **DELETE** `/api/admin/guides/{guideId}/activities/{activityId}` _(Admin only)_ – Delete an activity

---

### Access (Admin)

- **GET** `/api/admin/guides/{guideId}/access` – List users with access to a guide
- **POST** `/api/admin/guides/{guideId}/access` – Grant access to a user for a guide
- **DELETE** `/api/admin/guides/{guideId}/access/{userId}` – Revoke a user’s access

---

### Audiences (Admin)

- **GET** `/api/admin/audiences` – List all audiences
- **GET** `/api/admin/audiences/{id}` – Get audience by ID
- **POST** `/api/admin/audiences` – Create a new audience
- **PUT** `/api/admin/audiences/{id}` – Update an audience
- **DELETE** `/api/admin/audiences/{id}` – Delete an audience

---

### Seasons (Admin)

- **GET** `/api/admin/seasons` – List all seasons
- **GET** `/api/admin/seasons/{id}` – Get season by ID
- **POST** `/api/admin/seasons` – Create a new season
- **PUT** `/api/admin/seasons/{id}` – Update a season
- **DELETE** `/api/admin/seasons/{id}` – Delete a season

---

### Mobilities (Admin)

- **GET** `/api/admin/mobilities` – List all mobilities
- **GET** `/api/admin/mobilities/{id}` – Get mobility by ID
- **POST** `/api/admin/mobilities` – Create a new mobility
- **PUT** `/api/admin/mobilities/{id}` – Update a mobility
- **DELETE** `/api/admin/mobilities/{id}` – Delete a mobility

---

### Roles (Admin)

- **GET** `/api/admin/roles` – List all roles
- **GET** `/api/admin/roles/{roleId}` – Get role by ID
- **POST** `/api/admin/roles` – Create a new role
- **PUT** `/api/admin/roles/{roleId}` – Update a role
- **DELETE** `/api/admin/roles/{roleId}` – Delete a role

---

## Testing

Run unit and integration tests :

```bash
./mvnw test
```

---

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes with meaningful messages
4. Push to your fork
5. Open a Pull Request against `develop`

---

## License

MIT License
