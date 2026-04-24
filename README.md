# Smart Campus Sensor & Room Management API

## Project Overview
This project is a RESTful API built using JAX-RS (Jersey) to manage university campus rooms and IoT sensors. It provides a robust set of endpoints for resource discovery, CRUD operations on rooms, sensor registration, and reading management.

The API is designed with modern RESTful architectural patterns, including **Sub-Resource Locators**, **HATEOAS-style discovery**, and **Centralized Exception Handling**.

## Build & Run Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Build the Project
```bash
mvn clean package
```

### Run the Application
The application starts an embedded Grizzly HTTP server.
```bash
mvn exec:java -Dexec.mainClass="com.smartcampus.Main"
```
Or run the JAR directly:
```bash
java -jar target/SmartCampusAPI-1.0-SNAPSHOT.jar
```
By default, the server runs at: `http://localhost:8080/api/v1`

---

## API Documentation & Endpoints

### 1. Service Discovery
Provides links to major resource collections.
- **URL**: `GET /api/v1/`
- **Response**: `200 OK` with JSON metadata.

### 2. Room Management
- **GET /rooms**: List all rooms.
- **POST /rooms**: Create a new room.
- **GET /rooms/{id}**: Get details of a specific room.
- **DELETE /rooms/{id}**: Delete a room (only if it has no sensors).

### 3. Sensor Management
- **GET /sensors**: List all sensors.
- **GET /sensors?type={type}**: Filter sensors by type (e.g., `CO2`, `Temperature`).
- **POST /sensors**: Register a new sensor to a room.

### 4. Sensor Readings (Sub-Resource)
Accessible via the parent sensor resource.
- **GET /sensors/{sensorId}/readings**: List all readings for a sensor.
- **POST /sensors/{sensorId}/readings**: Add a new reading to a sensor.
- **GET /sensors/{sensorId}/readings/{readingId}**: Get a specific reading.

---

## Sample cURL Commands

### Create a Room
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"id": "R101", "name": "Lab 1", "capacity": 30}'
```

### Register a Sensor
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{"id": "S1", "type": "CO2", "roomId": "R101"}'
```

### Add a Sensor Reading
```bash
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \
     -H "Content-Type: application/json" \
     -d '{"value": 450.5}'
```

### List All Sensors (Filtered)
```bash
curl "http://localhost:8080/api/v1/sensors?type=CO2"
```

### Delete a Room (Triggers 409 if sensor exists)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/R101
```

---

## Coursework Report Questions

### Part 1: Service Architecture & Discovery

#### Q1: JAX-RS Resource Lifecycle
In JAX-RS, resources are by default **request-scoped**. This means a new instance of the resource class is created for every incoming request and destroyed once the response is sent. This ensures thread safety and isolation. However, resources can be configured as **singletons** if they are stateless or manage shared state. Our implementation uses request-scoped resources to ensure each request is handled independently.

#### Q2: Benefits of HATEOAS
HATEOAS (Hypermedia As The Engine Of Application State) allows the server to provide links to related resources within the response body. This decouples the client from the server's hardcoded URI structure. By using the links provided in the discovery endpoint, clients can navigate the API dynamically, making the system more resilient to URI changes and enhancing discoverability.

### Part 4: Sub-Resource Locators

#### Q3: Why are Sub-Resource Locators better than Flat Resources?
Sub-resource locators (using `@Path` on a method that returns a locator class) provide several advantages over flat root resources:
1. **Context Sharing**: The parent resource can pass context (like `sensorId`) directly to the child resource constructor, avoiding repeated `@PathParam` declarations.
2. **Modularity**: It separates the concerns of different resource levels into dedicated classes, making the code cleaner and easier to maintain.
3. **Hierarchy Representation**: It naturally represents the parent-child relationship in the code structure, aligning with the hierarchical nature of REST resources.

### Part 5: Exception Handling & Filters

#### Q4: Benefits of ExceptionMappers
`ExceptionMapper<T>` allows for centralized error handling by mapping custom Java exceptions to specific HTTP responses. This keeps the resource logic clean of try-catch blocks and ensures a consistent error response format across the entire API. It promotes the DRY (Don't Repeat Yourself) principle and makes the API more professional by returning meaningful status codes (e.g., 409 for conflict, 422 for unprocessable entity).

#### Q5: Purpose of Filters
Filters are used for cross-cutting concerns that apply to many or all requests. Our `LoggingFilter` serves two purposes:
1. **Audit Trail**: Logs incoming requests and outgoing responses for debugging and monitoring.
2. **Performance Measurement**: By calculating the difference between start and end times, we can monitor the latency of different API endpoints, which is crucial for maintaining a responsive Smart Campus system.
