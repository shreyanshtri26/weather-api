# weather-api

Weather API for Pincode

## Description
A Spring Boot REST API to fetch and cache weather information for a given Indian pincode and date. The API uses OpenWeatherMap for geocoding and weather data, and stores results in an H2 in-memory database for caching and optimization.

## Features
- Get weather info for a pincode and date (current weather for demo)
- Caches pincode geolocation and weather data in DB
- Optimizes external API calls
- Testable via Postman, Swagger, or curl
- Includes unit and integration tests

## API Usage

### Endpoint
`POST /api/weather`

### Request Body (JSON)
```
{
  "pincode": "411014",
  "for_date": "2020-10-15"
}
```

### Response Body (JSON)
```
{
  "pincode": "411014",
  "for_date": "2020-10-15",
  "latitude": 18.5685,
  "longitude": 73.9158,
  "weatherData": { ... OpenWeatherMap weather JSON ... }
}
```

### Example curl
```
curl -X POST http://localhost:8080/api/weather \
  -H "Content-Type: application/json" \
  -d '{"pincode":"411014","for_date":"2020-10-15"}'
```

## Setup & Run

1. **Clone the repo**
2. **Configure API key**: The OpenWeatherMap API key is set in `src/main/resources/application.properties`.
3. **Build and run**:
   ```
   ./mvnw clean package
   ./mvnw spring-boot:run
   ```
   The API will be available at `http://localhost:8080/api/weather`.

## Testing & Coverage

- To run all tests:
  ```
  ./mvnw test
  ```
- To run tests with coverage (JaCoCo):
  ```
  ./mvnw clean test jacoco:report
  ```
  Coverage report: `target/site/jacoco/index.html`

## Tech Stack
- Java 17+
- Spring Boot 3
- Spring Data JPA
- H2 Database
- Lombok
- OpenWeatherMap API
- JUnit 5, Mockito, MockMvc, JaCoCo

## Notes
- For demo, only current weather is fetched (OpenWeatherMap historical API is paid).
- All data is cached in-memory (H2) and will reset on restart.
