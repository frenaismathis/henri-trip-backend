# HenriTrip Backend

Backend for the HenriTrip application, built with **Spring Boot 3**, **Java 17**, **PostgreSQL**, **Flyway**, and **JWT authentication**.

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

- `POST /api/auth/login` – Returns JWT token

### Users (Admin)

- `GET /api/admin/users`
- `GET /api/admin/users/{id}`
- `POST /api/admin/users`
- `PUT /api/admin/users/{id}`
- `DELETE /api/admin/users/{id}`

### Guides, Activities, Audiences, Seasons, Mobilities

- `GET /api/admin/{entity}`
- `GET /api/admin/{entity}/{id}`
- `POST /api/admin/{entity}`
- `PUT /api/admin/{entity}/{id}`
- `DELETE /api/admin/{entity}/{id}`

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
