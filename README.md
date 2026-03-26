# ✅ UC17: Spring Backend - REST Services and JPA for Quantity Measurement

## 📖 Description

UC17 transforms the Quantity Measurement Application into a **production-grade Spring Boot REST service**.

In UC16, the application implemented **JDBC-based database persistence**, but still required significant boilerplate code — manual connection management, raw SQL queries, manual JSON handling, and no HTTP exposure.

UC17 eliminates all of this by leveraging the **Spring Boot ecosystem**:

- Spring Boot replaces manual application wiring
- Spring Data JPA replaces all JDBC boilerplate
- Spring MVC exposes REST API endpoints
- Spring Security provides a security foundation
- Swagger/OpenAPI generates interactive API documentation
- Spring Boot Actuator enables health monitoring

UC17 is **fully backward compatible** with UC1–UC16. All business logic is preserved with identical results.

---

## 🎯 Objective

- Expose measurement operations as **RESTful HTTP endpoints**
- Replace manual JDBC code with **Spring Data JPA**
- Enable **dependency injection** via Spring IoC container
- Implement **centralized exception handling** using `@ControllerAdvice`
- Add **input validation** using Bean Validation annotations
- Generate **interactive API documentation** using Swagger UI
- Enable **application monitoring** using Spring Boot Actuator
- Maintain full compatibility with **UC1–UC16 functionality**

---

## 🏗 Updated Architecture
```
Client (curl / Postman / Swagger UI)
            ↓  HTTP Request (JSON)
    REST Controller Layer          @RestController
            ↓
    Service Layer                  @Service
            ↓
    Repository Layer               @Repository (Spring Data JPA)
            ↓
    Database (H2 In-Memory)        JPA / Hibernate
```

Spring IoC container manages all dependencies automatically — no manual object creation or wiring.

---

## 🔹 What Changed vs UC16

| Aspect | UC16 | UC17 |
|--------|------|------|
| HTTP Exposure | None | REST API on port 8080 |
| Data Access | Manual JDBC + HikariCP | Spring Data JPA (zero SQL) |
| Dependency Injection | Manual constructor injection | `@Autowired` by Spring |
| JSON Handling | Manual / Jackson setup | Auto-serialized by Spring MVC |
| Exception Handling | Per-method try-catch | Centralized `@ControllerAdvice` |
| Input Validation | Manual null checks | `@Valid`, `@NotNull`, `@Pattern` |
| API Documentation | None | Swagger UI at `/swagger-ui.html` |
| Transaction Management | Manual | `@Transactional` declarative |
| Monitoring | None | Actuator at `/actuator/health` |
| Testing | Manual integration tests | MockMvc + `@SpringBootTest` |

---

## 🔹 Layer-by-Layer Breakdown

### REST Controller Layer
```
QuantityMeasurementController
```

**Annotations used:**
- `@RestController` — marks class as REST controller, returns JSON automatically
- `@RequestMapping("/api/v1/quantities")` — base URL for all endpoints
- `@PostMapping`, `@GetMapping` — maps HTTP verbs to methods
- `@RequestBody` — deserializes incoming JSON to Java object
- `@PathVariable` — extracts value from URL path
- `@Valid` — triggers Bean Validation on incoming DTOs
- `@Operation`, `@Tag` — Swagger documentation annotations

**Supported endpoints:**

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/v1/quantities/compare` | Compare two quantities |
| POST | `/api/v1/quantities/convert` | Convert a quantity to another unit |
| POST | `/api/v1/quantities/add` | Add two quantities |
| POST | `/api/v1/quantities/subtract` | Subtract two quantities |
| POST | `/api/v1/quantities/divide` | Divide two quantities |
| GET | `/api/v1/quantities/history/operation/{operation}` | Get history by operation type |
| GET | `/api/v1/quantities/history/type/{measurementType}` | Get history by measurement type |
| GET | `/api/v1/quantities/history/errored` | Get all error records |
| GET | `/api/v1/quantities/count/{operation}` | Count successful operations |

---

### Service Layer
```
IQuantityMeasurementService
QuantityMeasurementServiceImpl
```

**Key changes from UC16:**
- Added `@Service` annotation — Spring registers and manages this bean
- Added `@Autowired` — Spring injects the repository automatically
- All operation methods now return `QuantityMeasurementDTO` instead of raw `QuantityDTO`
- Added 4 new history/count methods
- `convertDtoToModel()` replaces old `QuantityMapper` dependency
- Errors are saved to the database for audit tracking

---

### Repository Layer
```
QuantityMeasurementRepository  (extends JpaRepository)
```

This single interface **replaces all UC16 JDBC code** — no SQL, no ResultSet, no try-catch.

Spring Data JPA auto-generates implementation from method names:

| Method | Generated SQL |
|--------|--------------|
| `findByOperation(String op)` | `WHERE operation = ?` |
| `findByThisMeasurementType(String type)` | `WHERE this_measurement_type = ?` |
| `findByCreatedAtAfter(LocalDateTime date)` | `WHERE created_at > ?` |
| `findByIsErrorTrue()` | `WHERE is_error = true` |
| `countByOperationAndIsErrorFalse(String op)` | `COUNT(*) WHERE operation = ? AND is_error = false` |

`JpaRepository` also provides for free: `save()`, `findAll()`, `findById()`, `deleteById()`, `count()`

---

### Model / Entity Layer
```
QuantityMeasurementEntity  (JPA Entity)
```

**JPA annotations added:**
- `@Entity` — maps class to database table
- `@Table(name = "quantity_measurements")` — specifies table name with indexes
- `@Id` + `@GeneratedValue` — auto-increment primary key
- `@Column` — column constraints
- `@PrePersist` / `@PreUpdate` — auto-sets `createdAt` and `updatedAt` timestamps

**Lombok annotations added:**
- `@Data` — generates all getters, setters, equals, hashCode, toString
- `@NoArgsConstructor` — required by JPA
- `@AllArgsConstructor` — full constructor

---

### DTO Layer

**New/Updated DTOs:**

| Class | Purpose |
|-------|---------|
| `QuantityDTO` | Input DTO with `@NotNull`, `@NotEmpty`, `@Pattern`, `@AssertTrue` validation |
| `QuantityInputDTO` | Wraps two `QuantityDTO`s for REST request body |
| `QuantityMeasurementDTO` | Rich response DTO with `fromEntity()`, `toEntity()`, `fromEntityList()` factory methods |
| `OperationType` | Enum: `ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`, `COMPARE`, `CONVERT` |

---

### Exception Handling
```
GlobalExceptionHandler  (@RestControllerAdvice)
```

Centralised handler for all exceptions across all controllers:

| Handler | Handles | HTTP Status |
|---------|---------|-------------|
| `handleValidationException` | `@Valid` failures | 400 Bad Request |
| `handleQuantityException` | `QuantityMeasurementException` | 400 Bad Request |
| `handleGlobalException` | All other exceptions | 500 Internal Server Error |

**Error response format:**
```json
{
  "timestamp": "2026-03-18T11:15:10",
  "status": 400,
  "error": "Quantity Measurement Error",
  "message": "Unit must be valid for the specified measurement type",
  "path": "/api/v1/quantities/add"
}
```

---

### Security Configuration
```
SecurityConfig  (@Configuration)
```

Configured to **allow all requests** for development. Provides the foundation for adding JWT or OAuth2 authentication in future use cases. CSRF disabled for stateless REST API. H2 console frame access enabled.

---

## ⚙️ application.properties
```properties
spring.application.name=quantity-measurement-app

# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:quantitymeasurementdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs

# Actuator
management.endpoints.web.exposure.include=health,info,metrics

# Server
server.port=8080
```

---

## 🔥 Key Maven Dependencies Added
```xml
spring-boot-starter-web          <!-- Tomcat + Spring MVC + REST + JSON -->
spring-boot-starter-data-jpa     <!-- Hibernate ORM + Spring Data -->
spring-boot-starter-validation   <!-- @NotNull, @Valid etc. -->
spring-boot-starter-security     <!-- Security foundation -->
spring-boot-starter-actuator     <!-- /actuator/health, /metrics -->
h2                               <!-- In-memory database -->
lombok                           <!-- @Data, @Builder etc. -->
springdoc-openapi-starter-webmvc-ui  <!-- Swagger UI -->
spring-boot-starter-test         <!-- MockMvc + SpringBootTest -->
```

---

## 🚀 How to Run
```bash
# Build and compile
mvn clean compile

# Run all tests
mvn test

# Start Spring Boot application
mvn spring-boot:run
```

---

## 🧠 Spring Boot Concepts Learned

| Annotation | Layer | What it does |
|-----------|-------|-------------|
| `@SpringBootApplication` | Main class | Auto-config + component scan + bean registration |
| `@RestController` | Controller | Marks REST controller, returns JSON automatically |
| `@RequestMapping` | Controller | Sets base URL for all endpoints |
| `@PostMapping` / `@GetMapping` | Method | Maps HTTP POST/GET to method |
| `@RequestBody` | Param | Deserializes JSON body to Java object |
| `@PathVariable` | Param | Extracts `{value}` from URL |
| `@Valid` | Param | Triggers Bean Validation |
| `@Service` | Service class | Registers as Spring service bean |
| `@Repository` | Repository | Registers JPA repo, enables exception translation |
| `@Autowired` | Field | Spring injects matching bean automatically |
| `@Entity` | Model class | Maps class to database table |
| `@Id` + `@GeneratedValue` | Field | Primary key with auto-increment |
| `@PrePersist` / `@PreUpdate` | Method | Lifecycle hooks for timestamps |
| `@Data` (Lombok) | Class | Generates all boilerplate |
| `@Builder` (Lombok) | Class | Enables builder pattern |
| `@RestControllerAdvice` | Exception class | Global exception handling |
| `@ExceptionHandler` | Method | Handles specific exception type |
| `@WebMvcTest` | Test class | Controller unit test — no DB |
| `@SpringBootTest` | Test class | Full integration test |
| `@MockBean` | Test field | Mocks a Spring bean in tests |

---

## 📤 Postconditions

- Spring Boot application runs on embedded Tomcat at port 8080 ✅
- REST endpoints accessible at `http://localhost:8080/api/v1/quantities/*` ✅
- JPA entities auto-mapped to H2 database tables ✅
- Swagger UI accessible at `http://localhost:8080/swagger-ui.html` ✅
- H2 console accessible at `http://localhost:8080/h2-console` ✅
- All UC1–UC16 business logic preserved with identical results ✅
- Dependency injection managed by Spring IoC container ✅
- Exception handling centralized through `@ControllerAdvice` ✅
- Actuator endpoints available for monitoring ✅

---

## 🚀 Architectural Evolution

| Use Case | Capability Added |
|----------|-----------------|
| UC1 | Feet equality |
| UC2 | Inch equality |
| UC3 | Generic Length |
| UC4 | Yard support |
| UC5 | Unit conversion |
| UC6 | Unit addition |
| UC7 | Explicit target addition |
| UC8 | Standalone units |
| UC9 | Weight management |
| UC10 | Generic quantity architecture |
| UC11 | Volume measurement |
| UC12 | Subtraction & Division |
| UC13 | Centralized arithmetic logic |
| UC14 | Temperature measurement |
| UC15 | N-Tier architecture |
| UC16 | JDBC database persistence |
| **UC17** | **Spring Boot REST API + JPA** |

---

## 🔥 Key Achievement

UC17 transforms the application from a **database-backed console app** into a **fully functional REST microservice**.

The system now supports:
- HTTP-based access from any client (browser, Postman, curl, mobile app)
- Persistent operation history queryable via REST
- Interactive API documentation via Swagger
- Enterprise-grade dependency injection and transaction management
- Foundation for future enhancements: JWT security, cloud deployment, microservices

---
