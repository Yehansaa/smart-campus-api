# Smart Campus API

A RESTful Smart Campus backend implemented using **JAX-RS (Jersey)** and **Maven**.  
The API models three core domain entities:

- **Rooms**: physical spaces in the campus (e.g., labs, classrooms)
- **Sensors**: hardware devices installed in rooms (e.g., CO2, temperature)
- **SensorReadings**: time-series measurements produced by sensors

The system employs in-memory data structures with a high level of REST design, validation, error management and logging.

---

## 1) Project Overview

This system offers a versioned RESTful API to handle core Smart Campus resources, such as rooms, sensors, and sensor readings.

The API supports the following operations:

- Creating, retrieving, listing, and deleting rooms
- Adding sensors and retrieving them with optional filtering (e.g., by type)
- Control of sensor readings using nested resource endpoints.

The architecture is based on the principles of REST, where resource-based URLs are used, proper use of HTTP methods, and the communication is based on JSON.

Implementation highlights:

- **JAX-RS annotations** (`@Path`, `@GET`, `@POST`, `@DELETE`, `@Consumes`, `@Produces`)
- **Jersey runtime** for request handling and JSON serialization
- **Maven** for project build and dependency management
- **In-memory data structures** (e.g., maps and lists) to store runtime data.

---

## 2) How to Run the Project

### Requirements

- **Java 17+**
- **Apache Maven 3.8+**

### Build

```bash
mvn clean install
```

### Run

```bash
mvn exec:java
```

### Base URL

```text
http://localhost:8080/api/v1
```

> Once started, the API entry point is available under `/api/v1`.

---

## 3) Discovery Endpoint  

Example response:

{
  "version": "v1",
  "contact": "admin@smartcampus.com",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}

This endpoint offers API metadata and navigation links, aiding with REST discoverability.

## 4) Sample API Usage (curl)

### 1. Create a room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"R101\",\"name\":\"Lab A\",\"capacity\":40}"
```

### 2. Get all rooms

```bash
curl http://localhost:8080/api/v1/rooms
```

### 3. Create a sensor linked to a room

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"S-CO2-1\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"R101\"}"
```

### 4. Filter sensors by type

```bash
curl "http://localhost:8080/api/v1/sensors?type=CO2"
```

### 5. Add a reading to a sensor

```bash
curl -X POST http://localhost:8080/api/v1/sensors/S-CO2-1/readings \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"RD-1\",\"timestamp\":1713600000,\"value\":550.2}"
```

---

## 5 ) API Design Overview

### Resource hierarchy

The API follows a clear parent-child relationship:

- `/rooms` -> room collection
- `/sensors` -> sensor collection
- `/sensors/{sensorId}/readings` -> readings belonging to a specific sensor

This structure keeps relationships explicit and makes traversal intuitive.

### REST principles applied

- **Resource-oriented URLs** with nouns (`rooms`, `sensors`, `readings`)
- **HTTP methods as actions** (`GET`, `POST`, `DELETE`)
- **Stateless interactions** per request
- **Uniform interface** via JSON request/response media type
- **Semantically meaningful status codes** for success/failure scenarios

### JSON usage

- `@Produces(MediaType.APPLICATION_JSON)` standardizes output format
- `@Consumes(MediaType.APPLICATION_JSON)` enforces valid JSON inputs
- Error responses are structured JSON objects (e.g., `{ "error": "..." }`)

### Status code strategy

- `200 OK`: successful retrieval
- `201 Created`: successful creation
- `204 No Content`: successful deletion without body
- `403 Forbidden`: operation blocked by resource state
- `409 Conflict`: domain constraint conflict
- `422 Unprocessable Entity`: payload contains an invalid linked reference
- `500 Internal Server Error`: unexpected error at runtime.

---

## 6) Report - Answers to Coursework Questions

### Question 1: JAX-RS Resource Lifecycle and In-Memory State

In the standard JAX-RS, resource classes have a default lifecycle of **per-request**: a new resource object is usually generated each time an incoming HTTP request is received unless otherwise explicitly stated. This model has the advantage that request-scoped instance fields are automatically isolated and do not share mutable state at the resource object level.

However, this does not eliminate concurrency issues when shared in-memory data structures (such as maps or lists) are used. Data in this project is stored in shared collections which can be accessed by multiple threads simultaneously.

In order to avoid issues like race condition or inconsistent data, the following is required:

- Use thread-safe data structures (e.g., `ConcurrentHashMap`).
- When a list might be accessed by more than one thread, ensure it is handled safely.
- Make operations straightforward and manageable.
- Avoid exposing internal mutable data directly

So, object instances are safeguarded by per-request resource lifecycle, but explicit synchronization strategy is still necessary in shared repositories.

Therefore, the per-request lifecycle enhances isolation on the object level, but when collaborating with shared data, appropriate synchronization is needed.

---

### Question 2: HATEOAS and Benefits of Hypermedia in REST APIs

Hypermedia (HATEOAS) is the presence of links in API responses that indicate the next actions that can be taken by the client. 
It is regarded as an advanced RESTful feature since the server can regulate the interaction with the API by the clients.

Benefits :

A primary benefit is that it allows runtime discoverability, allowing clients to navigate the API by following links instead of depending only on external documentation. It also minimizes reliance on fixed endpoint structures, which makes the system more adaptable and maintainable.

Also, this method enhances clarity to indicate what actions can be used depending on the present condition of a resource. In general, hypermedia will enable the API to be more self-descriptive and less dependent on the static documentation, as well as to be more usable to the developers.

---

### Question 3: Returning IDs vs Full Objects in API Responses

Sending back room IDs alone lowers the data sent across the network and this enhances performance, particularly where large data sets are involved. However, this method means that clients need to make extra requests to obtain complete details, which adds complexity and latency.

On the other hand, returning full room objects provides all necessary information in a single response, making it easier for clients to process and display data. The disadvantage is that it will expand the size of response that can affect performance.

So, the decision is based on the use case:

- Simple listing or navigation is more efficient with the return of IDs.
- Returning full objects is more convenient when detailed information is required

A balanced approach may include returning partial data or using pagination to manage response size effectively.

---

### Question 4: Idempotency of the DELETE Operation

Yes, the DELETE operation is idempotent. In case of a valid `DELETE /rooms/{id}` request, the room will be deleted in the system as long as all business requirements are met.
When the request is repeated, then no other changes can be made since the room does not exist any longer. Therefore, the state of the system remains unchanged after the initial deletion.


In this implementation:

1. The initial DELETE request will delete the room and send a response of 204 No Content.
2. Any new DELETE requests on the same room will respond with a 404 Not Found, as the resource has been deleted.

The response status can vary, but the system will not change after the initial request. The room has already been deleted and further requests do not create any further modifications. Thus, DELETE operation meets the criterion of idempotency.

---

### Question 5: Effects of @Consumes(MediaType.APPLICATION_JSON)

The `@Consumes(MediaType.APPLICATION_JSON)` annotation limits the endpoint to only accept requests with a `Content-Type` of JSON. When a client provides data in a non-text format (e.g. text/plain or application/xml), the JAX-RS runtime (e.g. Jersey) tries to find an appropriate message body reader to match the request format.

In case of a mismatched reader to the given media type, the request is not passed on to the resource method. In these situations, JAX-RS will automatically send an HTTP `415 Unsupported Media Type` response.

This behaviour guarantees that the API has a strict and predictable contract, avoiding ambiguity in data processing and consistency in the interpretation of request payloads.

---

### Question 6: Query Parameters vs Path Parameters for Filtering
  
When implementing optional filtering on a collection resource, the concept of optional filtering is more aligned with the principles of RESTful design by using the `@QueryParam`.
This method has `/sensors` as the primary collection endpoint and parameters like 
`?type=CO2` as a filtered view of that collection.

There are various reasons why query parameters are usually desirable to filter and search. To begin with, they offer semantic clarity, with the path being used to find the resource, and the query string refines the result set.
Second, they allow composability, meaning that one can use several filters in a single request (e.g., ?type=CO2&status=ACTIVE). Also, query parameters are flexible, in that they do not require specifying different endpoints to each filtering criterion.

Conversely, putting filters in the path of the URL (e.g. /sensors/type/CO2) may result in an proliferation of rigid endpoints and less flexibility when combining multiple conditions. 

Generally, the query parameter approach leads to a more maintainable and scalable API design.

---

### Question 7: Benefits of the Sub-Resource Locator Pattern

The Sub-Resource Locator pattern can be used to deal with nested resources using dedicated, specialized classes rather than all logic in one controller. In this project, you can request to /sensors/{sensorId}/readings and this request is delegated to a particular resource class that only handles sensor readings.

This approach offers several architectural advantages. Firstly, it improves separation of concerns, as each class is responsible for a specific part of the system. Secondly, it improves maintainability, as smaller and more focused classes are simpler to understand, test, and modify. It also enables scalability with extra nested resources being added without overcomplicating the main resource class.

Additionally, the parent resource may be able to transfer contextual information, including the sensorId, to the sub-resource. This will make sure that every operation is properly linked to the corresponding parent entity.

In contrast, when all the nested routes are contained in a single large controller, this may cause too much complexity and the code may be more difficult to maintain, more tightly coupled and more prone to bugs.

---

### Question 8: Why HTTP 422 is Preferred Over 404 for Invalid References
 
The use of HTTP `404 Not Found` is typically used when the URL requested is not found.
In this scenario, the request is addressed to a valid endpoint (e.g., POST /sensors), and the JSON format is correct. The problem is that one of the fields used in the request (e.g., roomId) is a reference to the resource that does not exist.

In this case, HTTP 422 Unprocessable Entity is more suitable as it means that the server knows the request but is unable to handle it because of logical or business rule violation. This gives the client better feedback on the issue, showing that it is in the request data, not the endpoint itself.

---

### Question 9: Security Risks of Exposing Stack Traces
 
It can be extremely dangerous to expose internal Java stack traces to API users. A stack trace can provide information on the internal structure of the system and its behavior.

As an example, a hacker can discover:

- Name of classes and packages used in the application.
- The structure and logic of the internal systems.
- Frameworks or libraries being used.
- Possible weak points or errors in the code.

This data may facilitate easier identification of vulnerabilities and target attacks on an attacker. To avoid this, it is safer to send a bare error message (like a generic 500 error) to the client, and maintain detailed error logs only on the server side, to debug the error.

---

### Question 10: Advantages of Using JAX-RS Filters for Logging
 
JAX-RS filters are more effective in logging since logging is a cross-cutting concern, that is, applicable to every aspect of the application.

Logging can be managed at a single point with the use of filters instead of repeating logging code in each resource method. This method comes with a number of benefits. It makes sure that every request and response is logged and to minimize duplication of code and resources classes are restricted to business logic.

Also, filters simplify the process of updating or enhancing logging in future, as only a single location is required to make changes. This leads to cleaner, more maintainable and reliable code.

---

## 7) Error Handling Design

The API implements custom exceptions to deal with various error conditions and relates them to relevant HTTP status codes. This will make sure every kind of error is well communicated to the client.

- **`RoomNotEmptyException` -> `409 Conflict`**  
  This happens when an attempt is made to delete a room which still has active sensors. This is blocked to ensure the data integrity.

- **`LinkedResourceNotFoundException` -> `422 Unprocessable Entity`**  
  This is raised when a sensor is created with a non-existent `roomId`. The request is properly structured but has incorrect data.

- **`SensorUnavailableException` -> `403 Forbidden`**  
  This happens when a reading is sent to a sensor in a `MAINTENANCE` state and cannot receive new data.

- **Global Exception Handler (`ExceptionMapper<Throwable>`) -> `500 Internal Server Error`**  
  This will serve as a backup system to prevent unexpected bugs. It makes sure that the internal information, including stack traces, is not exposed to the client.

### Error response format

Any errors are sent back in a regularized JSON format, e.g.:

```json
{
  "error": "Room has active sensors"
}
```

This organized format simplifies the processing and understanding of error responses, enhancing the usability of the API in general.

---

## 8) Technologies Used

- **Java**
- **JAX-RS (Jersey)**
- **Maven**

---

## Notes

- The system is based on in-memory storage; hence, all data is lost once the application is re-launched.
- The design option is consistent with the coursework requirements that do not allow using external databases.
- The API shows important REST concepts such as validation, structured error handling and logging using JAX-RS filters.
