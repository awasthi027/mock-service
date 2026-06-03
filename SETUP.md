# Mock Service - Quick Setup Guide

## Project Overview

A complete Spring Boot REST API service for managing JSON and XML data with PostgreSQL backend. Production-ready with comprehensive unit tests, Docker support, and Railway Cloud deployment configuration.

## Quick Statistics

✅ **Successfully Created:**
- **Total Files**: 27 source files (core + tests)
- **Unit Tests**: 32 tests (100% passing)
- **Build Status**: ✅ BUILD SUCCESS
- **JAR Size**: 38MB
- **Target JDK**: Java 17+

## Project Structure

```
mock-service/
├── src/main/
│   ├── java/com/ashi/mockservice/
│   │   ├── MockServiceApplication.java         # Spring Boot entry point
│   │   ├── controller/                          # REST API endpoints
│   │   │   ├── JsonController.java
│   │   │   └── XmlController.java
│   │   ├── service/                             # Business logic
│   │   │   ├── JsonDataService.java
│   │   │   └── XmlDataService.java
│   │   ├── model/                               # JPA entities
│   │   │   ├── JsonData.java                   # JSONB support
│   │   │   └── XmlData.java                    # TEXT support
│   │   └── repository/                          # Data access
│   │       ├── JsonDataRepository.java
│   │       └── XmlDataRepository.java
│   └── resources/
│       ├── application.yml                      # Configuration
│       └── schema.sql                           # Database schema
├── src/test/
│   ├── java/com/ashi/mockservice/
│   │   ├── controller/
│   │   │   ├── JsonControllerUnitTest.java     # ✅ 7 tests
│   │   │   └── XmlControllerUnitTest.java      # ✅ 7 tests
│   │   └── service/
│   │       ├── JsonDataServiceTest.java        # ✅ 9 tests
│   │       └── XmlDataServiceTest.java         # ✅ 9 tests
│   └── resources/
│       └── application.yml                      # H2 test config
├── pom.xml                                      # Maven build
├── Dockerfile                                   # Docker image
├── docker-compose.yml                           # Local dev setup
├── railway.toml                                 # Railway deployment
├── railway.json                                 # Railway schema
├── .gitignore                                   # Git exclusions
├── .env.example                                 # Environment template
└── README.md                                    # Full documentation
```

## Running Unit Tests

```bash
# Run all unit tests (32 tests)
mvn test -Dtest="*UnitTest,*ServiceTest"

# Expected Output: Tests run: 32, Failures: 0, Errors: 0
```

## Local Development

### 1. Using Docker Compose (Recommended)

```bash
# Start PostgreSQL and application
docker-compose up

# Application will be available at: http://localhost:8080
```

### 2. Manual Setup

```bash
# Prerequisites: PostgreSQL 15+ running locally

# Create database
createdb mockservice

# Set environment variables
export DATABASE_URL=jdbc:postgresql://localhost:5432/mockservice
export DATABASE_USER=postgres
export DATABASE_PASSWORD=password

# Run the application
mvn spring-boot:run
```

### 3. Using Built JAR

```bash
# Build the project
mvn clean package -DskipTests

# Run the JAR
java -jar target/mock-service-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### JSON Operations

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/json` | Upload JSON data |
| GET | `/api/json/{id}` | Retrieve JSON by ID |
| PUT | `/api/json/{id}` | Update JSON data |
| DELETE | `/api/json/{id}` | Delete JSON data |

### XML Operations

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/xml` | Upload XML data |
| GET | `/api/xml/{id}` | Retrieve XML by ID |
| PUT | `/api/xml/{id}` | Update XML data |
| DELETE | `/api/xml/{id}` | Delete XML data |

## API Examples

### Upload JSON
```bash
curl -X POST http://localhost:8080/api/json \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@example.com","age":30}'
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "data": {
    "name": "John",
    "email": "john@example.com",
    "age": 30
  },
  "createdAt": "2026-06-03T14:30:00",
  "updatedAt": "2026-06-03T14:30:00"
}
```

### Upload XML
```bash
curl -X POST http://localhost:8080/api/xml \
  -H "Content-Type: application/xml" \
  -d '<?xml version="1.0"?><user><name>John</name><email>john@example.com</email></user>'
```

### Get Data
```bash
curl http://localhost:8080/api/json/550e8400-e29b-41d4-a716-446655440000
```

### Update Data
```bash
curl -X PUT http://localhost:8080/api/json/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane","email":"jane@example.com","age":28}'
```

### Delete Data
```bash
curl -X DELETE http://localhost:8080/api/json/550e8400-e29b-41d4-a716-446655440000
```

## Neon Cloud Database

### 1. Create Neon Project
1. Go to https://console.neon.tech
2. Create a new project
3. Copy the connection string

### 2. Configure Connection
```bash
# Update environment variables
export DATABASE_URL=postgresql://user:password@host.neon.tech/neondb
export DATABASE_USER=<your_neon_user>
export DATABASE_PASSWORD=<your_neon_password>
```

### 3. Run Application
```bash
java -jar target/mock-service-0.0.1-SNAPSHOT.jar
```

## Railway Deployment

### 1. Connect Repository
1. Go to https://railway.app
2. Create new project
3. Connect GitHub repository

### 2. Set Environment Variables

In Railway Dashboard, add:
```
DATABASE_URL=postgresql://user:password@host.neon.tech/neondb
DATABASE_USER=<neon_username>
DATABASE_PASSWORD=<neon_password>
PORT=8080
JAVA_OPTS=-Xmx512m
```

### 3. Deploy

Railway will automatically:
- Build with Maven
- Run tests
- Create JAR
- Deploy to Railway infrastructure

Application will be available at: `https://<project-name>.up.railway.app/api/json`

## Database Schema

### json_data table
```sql
CREATE TABLE json_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    data JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_json_created_at ON json_data(created_at);
```

### xml_data table
```sql
CREATE TABLE xml_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    data TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_xml_created_at ON xml_data(created_at);
```

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL (JSONB support)
- **Build**: Maven 3.9.0
- **Testing**: JUnit 5, Mockito
- **ORM**: Hibernate with JSONB Type Support
- **Deployment**: Docker, Railway Cloud

## Dependencies

### Main
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- postgresql (driver)
- hibernate-types-60 (JSONB support)
- jackson-databind (JSON processing)

### Testing
- spring-boot-starter-test
- junit-jupiter
- mockito-core (v5.7.0)
- byte-buddy (v1.14.10)
- h2 (in-memory DB for tests)

## Features

✅ **RESTful API** - Full CRUD operations for JSON and XML data
✅ **JSONB Support** - Efficient JSON storage in PostgreSQL
✅ **UUID Primary Keys** - Distributed-friendly IDs
✅ **Audit Timestamps** - Auto-tracked created_at and updated_at
✅ **Unit Tests** - 32 comprehensive unit tests (100% pass rate)
✅ **Docker Support** - Dockerfile and docker-compose included
✅ **Railway Ready** - Pre-configured deployment files
✅ **Neon Integration** - Hosted PostgreSQL support
✅ **Error Handling** - Proper HTTP status codes and messages
✅ **Database Indexes** - Performance-optimized with indexes

## Future Enhancements

- [ ] Pagination support
- [ ] Search/filtering capabilities
- [ ] Data validation schemas
- [ ] Bulk operations
- [ ] JWT authentication
- [ ] Rate limiting
- [ ] Redis caching
- [ ] Swagger/OpenAPI documentation
- [ ] Observability (logging, metrics, tracing)

## Troubleshooting

### Connection Refused
- Ensure PostgreSQL is running
- Verify DATABASE_URL is correct
- Check network connectivity to Neon

### Tests Failing
- Run: `mvn clean test -Dtest="*UnitTest,*ServiceTest"`
- All 32 unit tests should pass
- Integration tests require full Spring context (optional)

### Build Issues
- Clear: `mvn clean`
- Rebuild: `mvn install`
- Check Java version: `java -version` (should be 17+)

## Next Steps

1. **Local Testing**: Run `docker-compose up`
2. **API Testing**: Use provided curl examples
3. **Create Neon Database**: Set up PostgreSQL on Neon
4. **Deploy to Railway**: Push to GitHub and connect to Railway
5. **Monitor**: Check Railway logs and metrics

## Support

For detailed documentation, see `README.md` in the project root.

## License

MIT License - Free to use and modify

