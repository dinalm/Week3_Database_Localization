# Shopping Cart Application with Database Localization

## Overview
A JavaFX shopping cart application with multi-language support (English, Finnish, Swedish, Japanese, Arabic) using MariaDB for localization and cart storage.

## Technologies
- Java 21
- JavaFX 21
- MariaDB
- Maven
- JUnit 5
- JaCoCo
- Docker
- Jenkins

## Database Setup
1. Install MariaDB
2. Run the schema:
```bash
mysql -u root -p < src/main/resources/schema.sql
```
3. Seed the localization data:
```bash
mysql -u root -p < src/main/resources/seed.sql
```

## Environment Variables
Set the following environment variables before running the application:

| Variable | Description | Example                                               |
|---|---|-------------------------------------------------------|
| `DB_URL` | MariaDB connection URL | `jdbc:mariadb://localhost:3306/shopping_cart_localization` |
| `DB_USER` | Database username | `username`                                            |
| `DB_PASSWORD` | Database password | `password`                                            |

In IntelliJ: Go to **Run → Edit Configurations → Environment Variables** and add the above.

In Jenkins: Add as **Secret text** credentials with IDs `db-url`, `db-user`, `db-password`.

## Running Locally
```bash
mvn javafx:run
```

## Running Tests
```bash
mvn test
```

## Docker
```bash
docker pull dinal1999/shopping-cart-app-v2:latest
docker run -e DISPLAY=host.docker.internal:0.0 dinal1999/shopping-cart-app-v2:latest
```

## Jenkins Pipeline
The pipeline performs:
1. Checkout from GitHub
2. Build with Maven
3. Run unit and integration tests
4. JaCoCo coverage report
5. Docker image build and push to Docker Hub

## Features
- Multi-language UI loaded from database
- RTL support for Arabic
- Cart calculations saved to database
- CI/CD pipeline with Jenkins
- Dockerized application