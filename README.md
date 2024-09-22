# Ktor sample application

## Overview

This repository contains a Ktor-based application with various routes and their corresponding tests. The application manages counters, providing endpoints to create, read, update, and delete counters. Testing is implemented using Kotlin's `kotest` and `ktor-server-tests` libraries.

## Project Structure

- **[src/main/kotlin](./src/main/kotlin)**: Contains the main application code, including route definitions and services.
- **[src/test/kotlin](./src/test/kotlin)**: Contains the test cases for the application routes.
- **[build.gradle.kts](build.gradle.kts)**: The Gradle build script for managing dependencies and build tasks.

## Endpoints

Also, there's swagger available at:
``http://0.0.0.0:8080/swagger``

### `GET /counters/read`

- **Description**: Retrieves the value of a specified counter.
- **Query Parameter**: `counter` (the name of the counter to read)
- **Responses**:
    - `200 OK`: When the counter exists. Response body includes counter's details.
    - `404 Not Found`: When the counter does not exist.

### `POST /counters/create`

- **Description**: Creates a new counter with the specified value.
- **Request Body**: JSON object containing `name` and `counter` values, which is being validated for blank value and for negative value.
- **Responses**:
    - `201 Created`: When the counter is successfully created. Response body includes the created counter's details.
    - `400 Bad Request`: When request validation failed.

### `PATCH /counters/increment`

- **Description**: Increments the value of a specified counter.
- **Query Parameter**: `counter` (the name of the counter to increment)
- **Responses**:
    - `200 OK`: When the counter exists and is incremented. Response body includes the new value.
    - `422 UnprocessableEntity`: When the counter does not exist.

### `DELETE /counters/delete`

- **Description**: Deletes the specified counter.
- **Query Parameter**: `counter` (the name of the counter to delete)
- **Responses**:
    - `204 No Content`: When the counter is successfully deleted.

### `GET /counters/all`

- **Description**: Retrieves a list of all counters.
- **Responses**:
    - `200 OK`: Returns a list of all counters in JSON format.

## Getting Started

To get started with this project, follow these steps:

1. **Clone the repository**:

   ```bash
   git clone https://github.com/vendelieu/ktor-sample.git
   ```

2. **Navigate to the project directory**:

   ```bash
   cd ktor-sample
   ```

3. **Build the project**:

   ```bash
   ./gradlew build
   ```

4. **Run the application**:

   ```bash
   ./gradlew run
   ```

## Running Tests

To run the tests, use the following command:

```bash
./gradlew test
```

## Dependencies

- Ktor: Web framework for building the application.
- Kotlin serialization: Kotlin Serde framework.
- Kotest: Testing framework for Kotlin.
- H2: In-memory database for testing purposes.
- Exposed: Database access library for Kotlin.
