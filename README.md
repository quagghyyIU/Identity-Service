## Identity Service (Spring Boot)

A lightweight JWT-based identity and authorization service built with Spring Boot 3. It issues, introspects, refreshes, and invalidates tokens, and manages users, roles, and permissions.

### Tech stack
- **Java**: 21
- **Build**: Maven (wrapper included)
- **Spring Boot**: Web, Data JPA, Validation, Security (OAuth2 Resource Server)
- **JWT**: Nimbus JOSE + JWT (HS512)
- **Database**: MySQL

### Prerequisites
- **Java 21**
- **MySQL 8+** running locally or reachable
- Optional: cURL or REST client (Postman/Insomnia)

### Configuration
Application config lives in `src/main/resources/application.yaml`.
- **Server**
  - **port**: `8080`
  - **context-path**: `/identity` (all endpoints are prefixed with this)
- **Datasource**
  - `spring.datasource.url`: `jdbc:mysql://localhost:3306/identity_service`
  - `spring.datasource.username`: `root`
  - `spring.datasource.password`: `root`
  - `spring.jpa.hibernate.ddl-auto`: `update` (creates/updates schema)
- **JWT**
  - `jwt.secret`: Base64/ASCII secret for HS512 signing
  - `jwt.valid-duration`: access token TTL in seconds (default `3600`)
  - `jwt.refresh-duration`: refresh window in seconds (default `360000`)

You can override any of the above via environment variables or JVM system properties per standard Spring Boot conventions.

### Quickstart
1. Ensure MySQL is running and a database named `identity_service` exists (or adjust the JDBC URL).
2. Configure credentials/secret as needed in `application.yaml` or environment.
3. Build and run:

```bash
# From project root
./mvnw clean package
./mvnw spring-boot:run
```

On Windows PowerShell:
```bash
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

The service will start at `http://localhost:8080/identity`.

### Security model
- **JWT**: HS512 signed. Claims include `sub` (username), `iss`, `iat`, `exp`, `jti`, and a space-delimited `scope`.
- **Authorities**: Derived from `scope`. Roles are emitted as `ROLE_{ROLE_NAME}`; permissions are emitted as their names. Authority prefix is not added.
- **Public endpoints** (POST): `/users`, `/auth/token`, `/auth/introspect`, `/auth/logout`, `/auth/refresh`.
- **Protected**: All others; `GET /users` additionally requires role `ADMIN`.

### API overview
- **Base URL**: `http://localhost:8080/identity`
- **Envelope**: All responses use:

```json
{
  "code": 1000,
  "message": "optional message",
  "result": { /* payload when applicable */ }
}
```

#### Auth
- **POST** `/auth/token` — obtain access token

Request:
```json
{ "username": "alice", "password": "P@ssw0rd!" }
```
Response:
```json
{
  "code": 1000,
  "result": { "token": "<JWT>", "authenticated": true }
}
```

- **POST** `/auth/introspect` — validate a token

Request:
```json
{ "token": "<JWT>" }
```
Response:
```json
{ "code": 1000, "result": { "valid": true } }
```

- **POST** `/auth/refresh` — refresh a token within refresh window

Request:
```json
{ "token": "<JWT>" }
```
Response:
```json
{ "code": 1000, "result": { "token": "<NEW_JWT>", "authenticated": true } }
```

- **POST** `/auth/logout` — invalidate a token (server-side)

Request:
```json
{ "token": "<JWT>" }
```
Response: `{"code":1000}`

Notes:
- Refresh stores the old token `jti` in an invalidation table; it cannot be reused.
- Logout stores the token `jti` in the invalidation table.

#### Users
- **POST** `/users` — register a new user (public)

Request:
```json
{
  "username": "alice",
  "password": "P@ssw0rd!",
  "email": "alice@example.com",
  "firstName": "Alice",
  "lastName": "Liddell",
  "dob": "2000-01-01"
}
```

- **GET** `/users` — list users (requires `ROLE_ADMIN`)

Headers:
```text
Authorization: Bearer <JWT>
```

- **GET** `/users/{userID}` — get user by ID (authenticated)
- **PUT** `/users/{userID}` — update user (authenticated)

Request (example):
```json
{
  "password": "NewP@ssw0rd!",
  "email": "alice.new@example.com",
  "firstName": "Alice",
  "lastName": "Wonder",
  "dob": "1999-12-31",
  "roles": ["ADMIN", "USER"]
}
```

- **DELETE** `/users/{userID}` — delete user (authenticated)
- **GET** `/users/myinfo` — current user profile (authenticated)

User response shape:
```json
{
  "id": "...",
  "username": "alice",
  "email": "alice@example.com",
  "firstName": "Alice",
  "lastName": "Liddell",
  "dob": "2000-01-01",
  "roles": [ { "name": "ADMIN", "description": "...", "permissions": [ { "name": "user:read" } ] } ]
}
```

#### Roles
- **POST** `/roles`

Request:
```json
{ "name": "ADMIN", "description": "Administrators", "permissions": ["user:read", "user:write"] }
```

- **GET** `/roles`
- **DELETE** `/roles/{role}`

#### Permissions
- **POST** `/permissions`

Request:
```json
{ "name": "user:read", "description": "Read users" }
```

- **GET** `/permissions`
- **DELETE** `/permissions/{permission}`

### JWT details
Tokens are signed with HS512 and include:
- **sub**: username
- **iss**: `quagghyy`
- **iat/exp**: issued-at and expiration per `jwt.valid-duration`
- **jti**: random UUID
- **scope**: space-delimited authorities (e.g., `"ROLE_ADMIN user:read"`)

### Error handling
Errors are returned with the same envelope. Examples:
```json
{ "code": 1001, "message": "UNAUTHENTICATED" }
```
Validation messages map to predefined error codes; details may be included in `message`.

### Development
- Build: `./mvnw clean package`
- Run: `./mvnw spring-boot:run`
- Test: `./mvnw test`

### Notes
- Public registration is enabled (`POST /users`). Adjust `SecurityConfig` if you need to restrict it.
- Consider rotating `jwt.secret` and shortening durations for production. 