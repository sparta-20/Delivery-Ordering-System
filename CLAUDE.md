# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.5 application built with Java 17, using MySQL as the database and JPA/Hibernate for data persistence. The project follows a standard layered architecture pattern.

## Development Commands

### Build and Run
- **Build project**: `./gradlew build`
- **Run application**: `./gradlew bootRun`
- **Run tests**: `./gradlew test`
- **Build Docker image**: `./gradlew bootBuildImage`

### Database Setup
- **Start PostgreSQL**: `docker-compose up -d`
- **Stop PostgreSQL**: `docker-compose down`

## Environment Setup

This project requires manual environment configuration before running:

1. **Copy configuration files**:
   - Copy `src/main/resources/application-sample.yml` to `src/main/resources/application.yml`
   - Copy `.env-sample` to `.env`

2. **Load environment variables**:
   - Run `source .env` in your terminal
   - Environment variables are automatically loaded by the application's custom loader in `DeliveryApplication.java`

3. **Required environment variables** (defined in `.env`):
   - `POSTGRES_HOST`: PostgreSQL server host
   - `POSTGRES_PORT`: PostgreSQL server port
   - `POSTGRES_DB`: Database name
   - `POSTGRES_USERNAME`: Database username
   - `POSTGRES_PASSWORD`: Database password
   - `POSTGRES_URL`: Complete JDBC URL

## Architecture

### Package Structure
- `com.delivery` - Main application package
- `com.delivery.test` - Test/example module demonstrating CRUD operations

### Layered Architecture
- **Controllers**: REST endpoints (`@RestController`)
- **Services**: Business logic (`@Service`, `@Transactional`)
- **Repositories**: Data access (`JpaRepository`)
- **Entities**: JPA entities with Lombok annotations

### Key Patterns
- Uses Lombok for boilerplate reduction (`@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`)
- Constructor injection with `@RequiredArgsConstructor`
- JPA entities with `GenerationType.IDENTITY`
- Custom environment variable loading in main application class

## Database Configuration

- **Database**: PostgreSQL 15 via Docker Compose
- **JPA Settings**:
  - `ddl-auto: update` (auto-creates/updates schema)
  - SQL logging enabled for development
  - PostgreSQL dialect configured
- **Connection**: Environment variable driven via application.yml placeholders

## Testing

- Uses JUnit 5 (`@SpringBootTest`)
- Test endpoint available at `/test/v1` for basic CRUD verification
- Run tests with `./gradlew test`

## Development Notes

- Environment variables are loaded both from system environment and `.env` file
- The application includes a custom `.env` file loader that runs before Spring Boot initialization
- All configuration is externalized through environment variables for different deployment environments