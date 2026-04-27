# 🏛️ Smart Campus Sensor & Room Management API

**Student:** Kivaane Anton Uthayakumar  
**UOW ID:** w2153364  
**IIT ID:** 20241925  
**Module:** 5COSC022W Client-Server Architectures  


---

## 📖 Project Overview

This project is a RESTful API built using **JAX-RS (Jersey)** to manage university campus rooms and IoT sensors. It provides a robust set of endpoints for resource discovery, CRUD operations on rooms, sensor registration, and reading management.

The API is designed with modern RESTful architectural patterns, including:
- **Sub-Resource Locators** - Hierarchical resource management
- **HATEOAS-style Discovery** - Self-descriptive API navigation
- **Centralized Exception Handling** - Consistent error responses
- **Request/Response Logging Filters** - Comprehensive observability

Additionally, the system supports multiple sensor types such as temperature, humidity, and CO2, along with filtering capabilities and validation mechanisms to ensure correct data handling.

**Technology Stack:**
- JAX-RS (Jersey 3.x)
- Grizzly HTTP Server (embedded)
- Java 17
- Maven 3.8+
- In-memory storage (HashMap/ArrayList)

---

## 🚀 Build & Run Instructions
You can either clone the repository using Git by running git clone https://github.com/your-username/SmartCampusAPI.git and navigating into the project folder, or download the project as a ZIP file, extract it, and open it in your preferred IDE such as NetBeans.

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

### Base URL
```
http://localhost:8080/api/v1
```

---

## 📡 API Documentation & Endpoints

### 1. Service Discovery
Provides links to major resource collections.

- **URL:** `GET /api/v1`
- **Response:** 200 OK with JSON metadata

---

### 2. Room Management

- `GET /api/v1/rooms` - List all rooms
- `POST /api/v1/rooms` - Create a new room
- `GET /api/v1/rooms/{id}` - Get details of a specific room
- `DELETE /api/v1/rooms/{id}` - Delete a room (only if it has no sensors)

---

### 3. Sensor Management

- `GET /api/v1/sensors` - List all sensors
- `GET /api/v1/sensors?type={type}` - Filter sensors by type (temperature, humidity, CO2, etc.)
- `POST /api/v1/sensors` - Register a new sensor to a room

---

### 4. Sensor Readings (Sub-Resource)

- `GET /api/v1/sensors/{sensorId}/readings` - Get all readings for a sensor
- `POST /api/v1/sensors/{sensorId}/readings` - Add a new reading
- `GET /api/v1/sensors/{sensorId}/readings/{readingId}` - Get a specific reading

---

## 🧪 Postman Testing Summary

The API was tested using Postman with a structured collection to validate both functional and error scenarios. 

**Functional Tests:**
- Rooms were successfully created and retrieved
- Multiple sensors were registered across different rooms using various types (temperature, humidity, CO2)
- Sensor readings were added and retrieved to verify sub-resource handling
- Filtering functionality was tested using query parameters

**Error Handling Tests:**
- Accessing non-existent resources (404 Not Found)
- Creating sensors with invalid room references (422 Unprocessable Entity)
- Attempting to delete rooms that still contain sensors (409 Conflict)
- Sending requests with incorrect Content-Type (500 Internal Server Error)

All tests confirmed the correct use of HTTP status codes and proper error responses.

---

## 📝 Sample cURL Commands

### 1. API Discovery
```bash
curl -X GET http://localhost:8080/api/v1
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "LIB-301",
    "name": "Library Study Room",
    "capacity": 20
  }'
```

### 3. Register a Sensor
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEMP-001",
    "type": "Temperature",
    "status": "ACTIVE",
    "roomId": "LIB-301"
  }'
```

### 4. Add a Sensor Reading
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{
    "value": 22.5
  }'
```

### 5. Filter Sensors by Type
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

### 6. Get All Readings for a Sensor
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

### 7. Delete a Room
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

---

## 📄 Coursework Report - Question Answers

---

## Part 1: Service Architecture & Setup

### Question 1: JAX-RS Resource Lifecycle and Data Management

In this project, JAX-RS resource classes follow the default per-request lifecycle, meaning that a new instance of each resource class is created for every incoming HTTP request and destroyed once the response is returned. This ensures that resource classes remain stateless and thread-safe as each request is handled independently without shared mutable state inside the resource instance.

Because of this lifecycle, resource classes cannot be used to store persistent data. To address this, the system uses a centralized in-memory storage mechanism implemented through a DataStore class, where data is maintained using static HashMaps for rooms, sensors, and sensor readings. These shared data structures allow all resource instances to access and modify the same application state across multiple requests.

<img width="1059" height="147" alt="image" src="https://github.com/user-attachments/assets/c599ec10-36db-41a1-a87f-aba05feeaf1e" />


*Figure 1: Centralized DataStore using static HashMaps for rooms, sensors, and readings to maintain shared application state across multiple requests.*

This design highlights an important architectural consideration: while JAX-RS resources are inherently stateless and recreated per request, the underlying data structures are shared across threads. As a result, care must be taken when managing these collections. In a production environment, thread-safe structures such as ConcurrentHashMap or synchronization techniques would be required to prevent race conditions, inconsistent updates, or data corruption.

Although this implementation uses simplified in-memory storage for demonstration purposes, it accurately reflects real-world challenges in managing shared state in concurrent server-side applications.

---

### Question 2: HATEOAS and Hypermedia Benefits

The use of hypermedia, known as HATEOAS (Hypermedia as the Engine of Application State), is a key characteristic of advanced RESTful API design. In this project, the discovery endpoint (`/api/v1`) provides links to major resources such as rooms and sensors, allowing clients to navigate the API dynamically.

Instead of relying on hardcoded URLs or external documentation, clients can follow links provided in API responses to understand available actions. This makes the API self-descriptive and reduces dependency on static documentation. For example, when a client accesses `/api/v1`, it receives a structured response containing available endpoints, which guides the client on how to interact with the system.

<img width="975" height="289" alt="image" src="https://github.com/user-attachments/assets/d7d6b2a8-b715-4865-b113-abeb8e7a4740" />


*Figure 2: HATEOAS-style response from API root endpoint showing navigable resource links (rooms, sensors, readings).*

This approach benefits client developers by improving flexibility and maintainability. If the API structure changes in the future, clients do not need to be modified, as they can adapt by following updated links returned by the server. Additionally, it reduces tight coupling between client and server, enhances scalability, and provides a more intuitive and discoverable interaction model for API consumers.

---

## Part 2: Room Management

### Question 1: Returning IDs vs Full Room Objects

When designing API responses, a decision must be made between returning only resource identifiers or returning full object representations. Returning only IDs reduces network bandwidth and payload size, which can be beneficial in large-scale systems. However, this approach requires additional API calls from the client to retrieve complete details, increasing complexity and latency on the client side.

In this project, full room objects are returned through the RoomResource, where each room includes fields such as id, name, capacity, and a list of associated sensorIds. This design allows clients to immediately access both room details and their related sensors without making additional requests.

<img width="975" height="509" alt="image" src="https://github.com/user-attachments/assets/943eb2e5-d10a-455e-81f6-6828abf44762" />


*Figure 3: API response showing full room object representation including room details and associated sensor IDs.*

This approach simplifies client-side processing by providing all necessary data in a single response, reducing the need for multiple API calls. Although this increases the response size slightly, it improves usability and efficiency, making it more suitable for applications where quick access to complete information is required, such as real-time smart campus systems.

---

### Question 2: DELETE Operation Idempotency

The DELETE operation in this API is idempotent, meaning that performing the same DELETE request multiple times results in the same final system state.

In this project, the DELETE functionality is implemented in the RoomResource class. When a client sends a DELETE request for a room, the system first checks whether the room exists and whether it contains any associated sensors. If the room has sensors linked to it, the API returns a 409 Conflict response, enforcing the business rule that a room cannot be deleted while it still contains sensors.

<img width="975" height="240" alt="image" src="https://github.com/user-attachments/assets/ece8d2d5-1eee-445a-86be-7d0ca2c2ecad" />


*Figure 4: API response showing 409 Conflict when attempting to delete a room that still contains active sensors.*

If the room exists and has no associated sensors, the first DELETE request successfully removes the room from the DataStore. If the same request is sent again, the API returns a 404 Not Found response because the room no longer exists.

<img width="975" height="165" alt="image" src="https://github.com/user-attachments/assets/db8280b0-0a61-4a82-a2d1-e3fd89552e1c" />


*Figure 5: API response showing 404 Not Found when attempting to delete a room that has already been removed.*

Despite the difference in responses, the final state of the system remains unchanged—the room is already deleted. Therefore, no additional side effects occur after the initial deletion, satisfying the definition of idempotency. This behavior ensures reliability and safety, especially in scenarios where duplicate requests may occur due to network retries or repeated client calls.

---

## Part 3: Sensor Operations & Linking

### Question 1: @Consumes Annotation Behavior

The `@Consumes(MediaType.APPLICATION_JSON)` annotation specifies that the API only accepts JSON-formatted request bodies. In this project, this annotation is used in resource classes such as SensorResource and RoomResource, ensuring that all POST requests must provide data in JSON format.

If a client attempts to send data in a different format, such as text/plain or application/xml, the JAX-RS runtime automatically rejects the request. This happens because JAX-RS uses MessageBodyReaders to convert incoming request data into Java objects. When the content type does not match the expected application/json, no suitable reader is found, and the request cannot be processed.

As a result, in this implementation, the server returns a 500 Internal Server Error because the request cannot be parsed into the expected Java object due to the unsupported format.

<img width="975" height="754" alt="image" src="https://github.com/user-attachments/assets/83935d87-06c9-4bb2-8cb7-1a382cbddef5" />


*Figure 6: POST request sent with Content-Type: text/plain instead of application/json resulting in a 500 Internal Server Error due to unsupported format.*

This mechanism enforces a strict API contract, improves data integrity, and ensures that the system operates reliably by handling only valid input formats. It also simplifies resource implementation, as developers can assume that incoming data is always in the correct format.

---

### Question 2: Query Parameters vs Path Parameters for Filtering

In this API, filtering is implemented using query parameters, such as `/api/v1/sensors?type=CO2`. Query parameters are specifically designed for filtering and searching operations, making them more suitable for retrieving subsets of a resource collection. In this project, this is implemented in the SensorResource class using `@QueryParam("type")`, allowing dynamic filtering of sensors without modifying the endpoint structure.

<img width="975" height="453" alt="image" src="https://github.com/user-attachments/assets/ffd35b02-5cc9-42ae-b137-be733783f625" />


*Figure 7: Filtering sensors using query parameter (type=temperature) successfully returns only matching sensor records.*

In contrast, path parameters are typically used to identify specific resources, such as `/sensors/{id}`. Using path parameters for filtering would make the API less flexible and harder to extend. Using path parameters for filtering would incorrectly treat the filter value as a resource identifier rather than a query condition, which violates RESTful design principles.

Query parameters allow multiple filters to be combined easily (e.g., `/sensors?type=CO2&roomId=R1`) without changing the endpoint structure. This makes the API more scalable, intuitive, and easier to maintain. Therefore, query parameters are the preferred approach for filtering and searching collections in RESTful design. This approach aligns with REST best practices where query parameters are used for optional filtering, sorting, and searching operations on collections.

---

## Part 4: Deep Nesting with Sub-Resources

### Question 1: Sub-Resource Locator Pattern Benefits

This project uses the Sub-Resource Locator pattern to manage nested resources such as sensor readings. Instead of handling all endpoints within a single resource class, the API delegates reading-related operations to a separate SensorReadingResource class. In this project, this is implemented in the SensorResource class using a method that returns an instance of SensorReadingResource for the path `/sensors/{sensorId}/readings`, effectively delegating all reading-related operations to the sub-resource.

<img width="975" height="532" alt="image" src="https://github.com/user-attachments/assets/d479c01f-d405-4965-9a63-14c8d17ee919" />


*Figure 8: Accessing sensor readings through sub-resource path /sensors/{sensorId}/readings demonstrates hierarchical resource handling using sub-resource locator.*

This design improves modularity by separating responsibilities into smaller, focused components. Each resource class handles a specific part of the system, making the code easier to understand, maintain, and extend. This pattern leverages JAX-RS runtime behavior where the parent resource acts as a dispatcher, and the actual request handling is performed by the returned sub-resource class. Additionally, the sub-resource locator allows contextual data, such as the sensorId, to be passed directly to the child resource. This reduces redundancy and avoids repeatedly extracting parameters in multiple methods.

Without this pattern, all nested routes would need to be handled in a single large controller class, leading to increased complexity, poor readability, and difficulty in maintenance. Therefore, this approach provides a clean, scalable, and well-organized architecture for managing hierarchical resources. This design aligns with RESTful principles by representing hierarchical relationships clearly and keeping resource classes focused and loosely coupled.

---

## Part 5: Advanced Error Handling, Exception Mapping & Logging

### Question 1: HTTP 422 vs 404 for Invalid References

HTTP 422 (Unprocessable Entity) is considered more accurate when the request is syntactically correct but contains invalid or inconsistent data.

In this project, when a client attempts to create a sensor using a non-existent roomId, the request itself is valid and properly formatted. The endpoint exists, and the JSON structure is correct. However, the request violates a business rule because the referenced room does not exist. In this project, this behavior is demonstrated when attempting to create a sensor with an invalid roomId, where the API validates the reference against existing rooms in the DataStore and rejects the request when the room does not exist. This validation occurs at the application logic level, where the system checks for referential integrity before processing the request.

<img width="975" height="204" alt="image" src="https://github.com/user-attachments/assets/d77368cb-47a3-4b5a-8c50-2add0e69ce08" />


*Figure 9: Attempting to create a sensor with a non-existent roomId results in validation failure, demonstrating the use of HTTP 422 for invalid request data.*

In contrast, a 404 Not Found response is used when the requested resource or endpoint itself does not exist. Since the endpoint is valid in this case, 422 provides a more precise representation of the error.

Using HTTP 422 helps clients clearly understand that the issue lies in the request data rather than the availability of the resource. Therefore, HTTP 422 is more semantically accurate because it indicates that the request is well-formed but contains logically incorrect data, aligning with RESTful error handling best practices.

---

### Question 2: Security Risks of Exposing Stack Traces

Exposing internal Java stack traces in API responses poses significant security risks. Stack traces can reveal sensitive information such as class names, package structures, method names, file paths, line numbers, and details about frameworks or libraries being used. In this project, stack traces are not directly exposed to clients. Instead, centralized exception handling is implemented using exception mappers, which convert internal exceptions into structured and user-friendly error responses.

Attackers can use this information to understand the internal architecture of the system and identify potential vulnerabilities. For example, knowing specific frameworks or versions can allow attackers to exploit known security weaknesses. Additionally, exposing file paths or line numbers could help attackers pinpoint exact locations in the codebase, making it easier to craft targeted attacks such as injection or denial-of-service exploits.

To mitigate this risk, the API uses exception mapping to return generic error messages to clients while logging detailed stack traces internally. This ensures that sensitive implementation details are not exposed, improving the overall security of the system. By hiding stack traces and returning sanitized responses, the API follows secure coding practices and prevents information leakage that could be exploited by malicious users.

<img width="975" height="191" alt="image" src="https://github.com/user-attachments/assets/57031426-9660-43ce-9160-d55b91372b68" />


*Figure 10: API returns sanitized error response without exposing internal stack trace, ensuring secure error handling.*

---

### Question 3: JAX-RS Filters for Cross-Cutting Concerns

Using JAX-RS filters for logging provides a centralized and consistent approach to handling cross-cutting concerns. Filters can intercept all incoming requests and outgoing responses, allowing logging to be applied uniformly across the entire application. In this project, a logging filter is used to intercept every HTTP request and response, capturing details such as the HTTP method, request URI, response status code, and processing time.

This eliminates the need to manually add logging statements in every resource method, reducing code duplication and improving maintainability. It also ensures that no endpoint is accidentally left without logging. This is achieved using JAX-RS ContainerRequestFilter and ContainerResponseFilter, which allow logging to be handled automatically without modifying individual resource methods.

Additionally, filters help maintain separation of concerns by keeping logging logic separate from business logic. This results in cleaner, more modular code and improves scalability, especially in larger systems where consistent logging is essential. This approach improves consistency, reduces human error, and ensures that logging is applied uniformly across all endpoints, making the system easier to debug and monitor.

<img width="975" height="273" alt="image" src="https://github.com/user-attachments/assets/062c6f52-750d-4126-bab5-a3ac1b0b056b" />


*Figure 11: Console output showing centralized logging filter capturing both incoming requests and outgoing responses across API endpoints.*

---

## 📺 Video Demonstration

A comprehensive video demonstration showcasing all API functionality, error handling, and testing scenarios has been recorded and will be submitted via the BlackBoard submission link as per the coursework requirements.

---

## 📝 License

This project is submitted as coursework for **5COSC022W Client-Server Architectures** at the **University of Westminster** in collaboration with the **Informatics Institute of Technology**.

**Academic Integrity Notice:** This code is provided for educational purposes and coursework submission only.

---

**End of README**
