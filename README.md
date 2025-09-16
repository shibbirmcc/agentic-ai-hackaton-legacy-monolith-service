# Legacy Monolith Service

A legacy Spring Boot 1.5 monolithic service managing Users and Orders with RabbitMQ messaging.

## Tech Stack

- Java 8
- Spring Boot 1.5.22.RELEASE
- Spring Data JPA with Hibernate
- Spring AMQP (RabbitMQ)
- H2 Database (default) / PostgreSQL
- Maven
- JaCoCo for test coverage (~60%)   

## Running Locally

### Option 1: H2 In-Memory Database (Default)

```bash
mvn spring-boot:run -Dspring.profiles.active=h2
```

The service will start on `http://localhost:8080` with H2 in-memory database.

### Option 2: With Docker (PostgreSQL + RabbitMQ)

1. Start dependencies:
```bash
docker compose up -d
```

2. Run the application:
```bash
./mvnw spring-boot:run
```

## Building and Testing

Build without tests:
```bash
./mvnw -q -DskipTests package
```

Run tests with coverage:
```bash
./mvnw test
```

Coverage report: `target/site/jacoco/index.html`

## API Endpoints

### Users
- `POST /users` - Create user
- `GET /users/{id}` - Get user by ID
- `GET /users` - Get all users
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Soft delete user

### Orders
- `POST /orders` - Create order (publishes to RabbitMQ)
- `GET /orders/{id}` - Get order by ID
- `GET /orders?userId={userId}` - Get orders by user
- `PUT /orders/{id}/status` - Update order status

### Health
- `GET /health` - Application health check

## Sample Requests

Create User:
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "fullName": "John Doe"
}
```

Create Order:
```json
{
  "userId": 1,
  "productName": "Legacy Widget",
  "quantity": 5,
  "price": 99.99,
  "notes": "Urgent delivery"
}
```

## Legacy Issues (To Be Addressed in Modernization)

1. **Deprecated APIs**: Spring Boot 1.5 reached EOL in 2019
2. **Security Gaps**: 
   - No authentication/authorization
   - Management endpoints exposed without security
   - No input validation framework
   - SQL injection risks in native queries
3. **Code Smells**:
   - Mixed responsibilities in service classes
   - Field injection instead of constructor injection
   - Business logic mixed with infrastructure concerns
   - Console logging instead of proper logging framework
4. **Infrastructure Issues**:
   - No connection pooling configuration
   - Missing circuit breakers for external services
   - No retry mechanisms for message publishing
   - Lack of distributed tracing
5. **Testing Limitations**:
   - Limited test coverage (~60%)
   - No integration tests for RabbitMQ
   - Missing API contract tests
6. **Operational Concerns**:
   - No metrics collection
   - Limited health checks
   - No mTLS support
   - Missing rate limiting
7. **Data Management**:
   - No database migrations (Flyway/Liquibase)
   - Eager fetching in some relationships
   - No caching strategy
8. **Message Handling**:
   - No dead letter queue configuration
   - Missing message versioning
   - No idempotency checks

## Database Schema

The application uses `schema.sql` and `data.sql` for database initialization. PostgreSQL-compatible schema with BIGSERIAL auto-increment columns:
- `users` - User information
- `orders` - Order details with foreign key to users

**Note**: Schema has been fixed to use PostgreSQL syntax (`BIGSERIAL`) instead of MySQL (`AUTO_INCREMENT`).

## Recent Fixes Applied

- ✅ **PostgreSQL Compatibility**: Fixed schema to use `BIGSERIAL` instead of MySQL `AUTO_INCREMENT`
- ✅ **JSON Circular References**: Added Jackson annotations (`@JsonManagedReference`/`@JsonBackReference`) to prevent infinite loops in User-Order relationships
- ✅ **Application Startup**: Fixed database initialization issues and verified all endpoints are functional

## RabbitMQ Integration

Orders are published to:
- Exchange: `legacy.exchange`
- Routing Key: `legacy.order.created`
- Queue: `legacy.order.queue`

## Notes

This is a deliberately legacy implementation showcasing common patterns and issues found in older enterprise Java applications. It serves as a baseline for modernization efforts.
