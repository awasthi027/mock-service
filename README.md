# Product Service (Java + Spring Boot)

A Spring Boot product microservice with PostgreSQL persistence.

## Features
- Post JSON
- Get JSON by id
- Update JSON
- Delete JSON


## Tech Stack
- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- JUnit 5 + Mockito + MockMvc

## API Endpoints
- `POST /api/json`
- `GET /api/json/{id}`
- `PUT /api/json/{id}`
- `DELETE /api/json/{id}`


## Request Example (Create/Update)
```json
{
  "name": "iPhone 15",
  "description": "Latest Apple smartphone",
  "price": 999.99,
  "category": "Electronics"
}
```

## Local Run
Set Neon connection string as environment variable and start app.

```bash
export DATABASE_URL="<your_neon_postgresql_connection_string>"

mvn clean test
mvn spring-boot:run
```

The app runs on `http://localhost:8080`.

## Quick API Check
```bash

curl --location 'https://mock-service-production-149f.up.railway.app/api/health'

```

```bash
curl --location 'https://mock-service-production-149f.up.railway.app/api/json' \
--header 'Content-Type: application/json' \
--data '{
  "name": "MacBook Pro 16",
  "description": "High-performance laptop with M3 Max chip, 16GB unified memory, 512GB SSD storage",
  "price": 2499.99,
  "category": "Electronics",
  "stock": 1
}'
```

```bash
curl --location 'https://mock-service-production-149f.up.railway.app/api/json/33636f49-bb21-4227-badd-59a52e709a28'
```

```bash
curl --location --request PUT 'https://mock-service-production-149f.up.railway.app/api/json/33636f49-bb21-4227-badd-59a52e709a28' \
--header 'Content-Type: application/json' \
--data '{
  "name": "MacBook Pro 17",
  "description": "High-performance laptop with M3 Max chip, 16GB unified memory, 512GB SSD storage",
  "price": 2599.99,
  "category": "Electronics",
  "stock": 1
}'

```

```bash
curl --location --request DELETE 'https://mock-service-production-149f.up.railway.app/api/json/33636f49-bb21-4227-badd-59a52e709a28'
```

```bash
curl --location 'https://mock-service-production-149f.up.railway.app/api/xml' \
--header 'Content-Type: application/xml' \
--data-raw '<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>John Doe</name><email>john@example.com</email></root>'

```

```bash

curl --location 'https://mock-service-production-149f.up.railway.app/api/xml/31402455-cd6e-4466-90b7-7cfe26a0b6da' \
--data ''

```

```bash

curl --location --request PUT 'https://mock-service-production-149f.up.railway.app/api/xml/31402455-cd6e-4466-90b7-7cfe26a0b6da' \
--header 'Content-Type: application/xml' \
--data-raw '<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>Ashish Awasthi</name><email>ashisha2@example.com</email></root>'

```

```bash
curl --location --request DELETE 'https://mock-service-production-149f.up.railway.app/api/xml/31402455-cd6e-4466-90b7-7cfe26a0b6da'
```

## Railway Deployment
This repo includes:
- `railway.toml`
- `Procfile`
- `system.properties`
- `Dockerfile`

### Steps
1. Push this project to GitHub.
2. In Railway, create a new project from this GitHub repo.
3. Add environment variable:
   - `DATABASE_URL` = your Neon connection string.
4. Deploy.
5. Railway sets `PORT` automatically and the app binds to it.

## Test
```bash
mvn test
```
