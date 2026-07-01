# Coffee Inventory Service - Complete Documentation

## Overview

This is a Spring Boot REST API service that retrieves coffee inventory information from an external Coffee Shop Database API with automatic fallback to a local database.

**Flow:**
1. Client calls → Controller → Service
2. Service calls → External Coffee Shop API (Primary)
3. If API fails → Falls back to Local Repository
4. Returns price and quantity sold

## Architecture

```
┌─────────────────────────┐
│    HTTP Client          │
│  (External or Browser)  │
└────────────┬────────────┘
             │ HTTP Request
             ▼
┌─────────────────────────────────────┐
│  InventoryController                │
│  - Validates input (non-empty)      │
│  - Maps HTTP status codes (200/404) │
│  - Delegates to service             │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  InventoryService (Interface)       │
│  coffeeByName(String)               │
│  coffeeByType(String)               │
└────────────┬────────────────────────┘
             │ implements
             ▼
┌─────────────────────────────────────────────┐
│  InventoryServiceImpl                        │
│  - Calls API client (primary)               │
│  - Maps API response to DTO                 │
│  - Falls back to repository if API fails    │
└──┬───────────────────────────────────────┬──┘
   │                                       │
   ▼                                       ▼
┌──────────────────────────┐   ┌────────────────────┐
│ CoffeeShopApiClient      │   │ InventoryRepository│
│ - Calls external API     │   │ - Local DB queries │
│ - Parses wrapper response│   │ - MySQL/H2         │
│ - Handles errors         │   │                    │
└──┬───────────────────────┘   └────────────────────┘
   │
   ▼
┌──────────────────────────┐
│ External Coffee Shop API │
│ (http://localhost:8080)  │
└──────────────────────────┘
```

## API Endpoints

### 1. Get Coffee by Name
**Request:**
```bash
GET /api/v1/inventory/by-name?coffeName=Gedeo
```

**Response (200 OK):**
```json
{
  "quantity": 150,
  "price": 12.99,
  "date": "2026-05-19T14:30:00"
}
```

**Error Responses:**
- `400 Bad Request` - coffeName is missing or empty
- `404 Not Found` - Coffee not found in API and local repository
- `500 Internal Server Error` - Unexpected server error

---

### 2. Get Coffee by Type
**Request:**
```bash
GET /api/v1/inventory/by-type?coffeType=DarkRoast
```

**Response (200 OK):**
```json
{
  "quantity": 200,
  "price": 15.50,
  "date": "2026-05-19T14:30:00"
}
```

**Error Responses:**
- `400 Bad Request` - coffeType is missing or empty
- `404 Not Found` - Coffee type not found in API and local repository
- `500 Internal Server Error` - Unexpected server error

---

### 3. Health Check
**Request:**
```bash
GET /api/v1/inventory/health
```

**Response (200 OK):**
```
Coffee Inventory Service is healthy
```

---

## Configuration

### application.properties

```properties
# Server
server.port=8082

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/coffee?useSSL=false
spring.datasource.username=root
spring.datasource.password=root1
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

# External Coffee Shop API
coffee-shop.api.base-url=http://localhost:8080
coffee-shop.api.timeout=5000
```

### How to Configure External API URL

Edit `application.properties` and change:
```properties
coffee-shop.api.base-url=http://your-coffee-shop-api-url:port
```

---

## External API Response Format

The system expects the external Coffee Shop API to return responses in this format:

```json
{
  "success": true,
  "message": "Coffee found successfully.",
  "data": [
    {
      "id": 4,
      "coffeeName": "Gedeo",
      "price": 12.99,
      "quantity": 6,
      "coffeeType": "PikeRosted"
    },
    {
      "id": 5,
      "coffeeName": "Gedeo",
      "price": 20.99,
      "quantity": 9,
      "coffeeType": "PikeRosted"
    }
  ]
}
```

**Field Mappings:**
- `data[0].coffeeName` → DTO `coffeeName`
- `data[0].coffeeType` → DTO `coffeeType`
- `data[0].price` → DTO `price`
- `data[0].quantity` → DTO `quantitySold` (stored as `quantity`)
- `data[0].id` → DTO `source` (appended for traceability)

**Parser Behavior:**
1. Checks `success` flag is `true`
2. Extracts first item from `data` array
3. Returns first item's data (ignores rest of array)
4. Returns empty Optional if `success=false` or `data` is empty

---

## Classes Overview

### DTOs (Data Transfer Objects)

#### `InventoryDTO`
- `quantity: int` - Total quantity (from API quantitySold)
- `price: Double` - Price per unit
- `date: LocalDateTime` - Timestamp

#### `CoffeeShopApiResponse`
- `coffeeName: String`
- `coffeeType: String`
- `price: Double`
- `quantitySold: Integer`
- `source: String` - API source tracking

#### `CoffeeItem`
- Maps individual items from API data array

### Entities

#### `CoffeeInventory`
- JPA entity for local database
- Fields: id, coffeeName, coffeeType, price, quantity, invDate

### Services

#### `InventoryService` (Interface)
```java
InventoryDTO coffeeByName(String coffeName);
InventoryDTO coffeeByType(String coffeType);
```

#### `InventoryServiceImpl` (Implementation)
- Calls `CoffeeShopApiClient` first (primary)
- Falls back to `InventoryRepository` if API fails
- Logs all operations for debugging

### Clients

#### `CoffeeShopApiClient` (Interface)
```java
Optional<CoffeeShopApiResponse> getCoffeeByName(String coffeeName);
Optional<CoffeeShopApiResponse> getCoffeeByType(String coffeeType);
```

#### `CoffeeShopApiClientImpl` (Implementation)
- Makes HTTP calls via RestTemplate
- Parses JSON wrapper response
- Handles errors gracefully
- Returns Optional for "not found"

### Common Library

#### `RestTemplateConfig`
```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }
}
```

---

## Testing

### Unit Tests

#### `CoffeeShopApiClientImplTest`
Tests the API client parsing logic:
- ✅ Successful response parsing
- ✅ Multiple items in array (takes first)
- ✅ Empty data array
- ✅ success=false flag
- ✅ Null responses
- ✅ Connection errors
- ✅ JSON parsing errors

Run:
```bash
mvn test -Dtest=CoffeeShopApiClientImplTest
```

#### `InventoryControllerTest`
Tests REST endpoint behavior:
- ✅ 200 OK with valid data
- ✅ 404 Not Found
- ✅ 400 Bad Request (missing/empty param)
- ✅ Health check

Run:
```bash
mvn test -Dtest=InventoryControllerTest
```

---

## Running the Application

### Prerequisites
- Java 17+
- MySQL (or H2 for in-memory testing)
- Maven 3.6+

### Build
```bash
cd C:\Users\jack\java\project\coffee\coffee-inventory
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Or with IDE:
1. Right-click `CoffeeInventoryApplication.java`
2. Select "Run"

### Verify It's Running
```bash
curl http://localhost:8082/api/v1/inventory/health
# Response: Coffee Inventory Service is healthy
```

---

## Error Handling

### Controller Level
- Validates input (non-empty strings)
- Returns appropriate HTTP status codes
- Logs all errors

### Service Level
- Tries API first
- Catches exceptions and falls back
- Logs API call status

### API Client Level
- Catches `RestClientException` (network errors)
- Catches JSON parsing errors
- Returns empty Optional on any failure

### Validation
- Input parameters cannot be null or empty (400 Bad Request)
- Trims whitespace before processing
- Logs warnings for invalid requests

---

## Logging

All operations are logged with appropriate levels:

- **INFO**: API calls, successful mappings, fallback decisions
- **WARNING**: Not found, API failures, invalid requests
- **SEVERE**: Unexpected errors
- **FINE**: Response details

View logs:
```bash
# In console during mvn spring-boot:run
# Or in application startup logs
```

---

## Troubleshooting

### Issue: Getting 404 Not Found

**Possible causes:**
1. Coffee name/type doesn't exist in either API or local DB
   - Verify data exists in Coffee Shop API
   - Verify data was saved to local MySQL

2. API is down but local DB has no fallback data
   - Solution: Ensure data is in local repository

3. External API URL is wrong
   - Check `coffee-shop.api.base-url` in application.properties
   - Verify API endpoint is `/api/v1/coffeehouse/searchByName` or `/api/v1/coffeehouse/by-type`

### Issue: Getting 400 Bad Request

**Possible causes:**
1. Missing query parameter
   - Ensure `?coffeName=` or `?coffeType=` is provided

2. Empty parameter value
   - Don't send `?coffeName=` (must have value)

**Fix:**
```bash
# ❌ Wrong
curl "http://localhost:8082/api/v1/inventory/by-name"
curl "http://localhost:8082/api/v1/inventory/by-name?coffeName="

# ✅ Correct
curl "http://localhost:8082/api/v1/inventory/by-name?coffeName=Gedeo"
```

### Issue: Getting 500 Internal Server Error

**Possible causes:**
1. Database connection failed
   - Check MySQL is running
   - Verify credentials in application.properties

2. Unexpected exception in service
   - Check application logs for stack trace
   - Enable debug logging if needed

---

## API Response Format

All successful responses return HTTP 200 with JSON body:

```json
{
  "quantity": <number>,
  "price": <decimal>,
  "date": "<ISO-8601 timestamp>"
}
```

Example:
```json
{
  "quantity": 150,
  "price": 12.99,
  "date": "2026-05-19T14:35:22.123456"
}
```

---

## Performance Considerations

### Connection Timeouts
- Connect timeout: 5 seconds (API call)
- Read timeout: 10 seconds (waiting for response)
- Adjust in `RestTemplateConfig` if needed

### Data Priority
1. **Primary (Preferred):** External Coffee Shop API
   - Real-time data from central repository
   
2. **Secondary (Fallback):** Local MySQL Repository
   - Cache/local copy for resilience

### Recommendations
- Regularly sync local repository with API data
- Monitor API response times
- Set up alerts for API failures
- Cache API responses if they're accessed frequently

---

## Future Enhancements

- [ ] Add pagination support for multiple results
- [ ] Add caching layer (Redis)
- [ ] Add metrics/monitoring (Micrometer)
- [ ] Add API documentation (Swagger/OpenAPI)
- [ ] Support bulk lookups
- [ ] Add search by price range
- [ ] Add data export (CSV, Excel)
- [ ] Add user authentication/authorization
- [ ] Add rate limiting
- [ ] Add request/response compression

