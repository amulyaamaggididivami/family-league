# Family League — Cricket Prediction Platform

A Spring Boot backend API for organizing family/social cricket prediction leagues. Users predict match outcomes and final standings; admins manage leagues, teams, matches, and publish results that trigger automated scoring.

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Framework | Spring Boot | 4.0.6 |
| Language | Java | 21 |
| Database | PostgreSQL | 18+ |
| Auth | JWT (JJWT HS256) | 0.12.6 |
| API Docs | Springdoc / Swagger UI | 2.8.9 |
| ORM | Spring Data JPA / Hibernate | via Boot parent |
| Security | Spring Security | via Boot parent |
| Email | Spring Mail / SMTP | via Boot parent |

---

## Prerequisites

- Java 21+
- PostgreSQL running locally
- Maven 3.8+ (or use the included `./mvnw`)

---

## Local Setup

### 1. Create the database

```bash
psql -U postgres -c "CREATE DATABASE \"family-league\";"
```

### 2. Configure `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/family-league
spring.datasource.username=postgres
spring.datasource.password=postgres

familyleague.jwt.secret=ZmFtaWx5bGVhZ3VlLXNlY3JldC1rZXktZm9yLWp3dC10b2tlbi1nZW5lcmF0aW9uLXYx
familyleague.jwt.expiration-ms=86400000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 3. Run

```bash
./mvnw spring-boot:run
```

Server starts on **http://localhost:9090**

### 4. Swagger UI

- **UI**: http://localhost:9090/swagger-ui.html
- **JSON**: http://localhost:9090/v3/api-docs

---

## Authentication

### Register
`POST /api/auth/register`
```json
{ "username": "john", "email": "john@x.com", "password": "pass1234", "fullName": "John" }
```
- Returns a JWT token
- User is automatically assigned **ROLE_USER**

### Login
`POST /api/auth/login`
```json
{ "username": "john", "password": "pass1234" }
```
- Returns a JWT token

### Use the token
Add to every request header:
```
Authorization: Bearer <token>
```
In Swagger UI — click **Authorize 🔒**, paste the token, click Authorize.

> **Note:** Swagger does not persist the token across page reloads. Re-authorize after every restart.

---

## Role System

| Role | Who | Access |
|---|---|---|
| `ROLE_USER` | All registered users | Predictions, leaderboard, read-only data |
| `ROLE_ADMIN` | Manually promoted users | Everything + create/manage leagues, teams, matches, results |

### Bootstrap the first admin

New users always get `ROLE_USER`. Promote via SQL:

```sql
-- 1. Seed the roles (if not done already)
INSERT INTO roles (id, name, description, created_at)
VALUES
  (gen_random_uuid(), 'ROLE_ADMIN', 'Administrator', now()),
  (gen_random_uuid(), 'ROLE_USER',  'Regular user',  now())
ON CONFLICT (name) DO NOTHING;

-- 2. Assign ROLE_ADMIN to your user
INSERT INTO user_roles (id, user_id, role_id, is_active, assigned_at)
VALUES (
  gen_random_uuid(),
  (SELECT id FROM users WHERE username = 'your-username'),
  (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
  true,
  now()
);
```

Then log in again to get a fresh token with the new role.

---

## API Reference

### Authentication — Public

| Method | Path | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user (auto ROLE_USER) |
| POST | `/api/auth/login` | Login, returns JWT token |

---

### Users — Authenticated

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/users` | User | List all users (paginated) |
| GET | `/api/users/{id}` | User | Get user by ID |
| PUT | `/api/users/{id}/profile` | User | Update own profile (fullName, avatarUrl) |
| DELETE | `/api/users/{id}` | User | Soft-delete account |

---

### Roles — Admin Only

| Method | Path | Description |
|---|---|---|
| GET | `/api/roles` | List all roles |
| GET | `/api/roles/{id}` | Get role by ID |
| POST | `/api/roles` | Create role |
| PUT | `/api/roles/{id}` | Update role |
| DELETE | `/api/roles/{id}` | Soft-delete role |

### User Role Assignment — Admin Only

| Method | Path | Description |
|---|---|---|
| GET | `/api/users/{userId}/roles` | List user's assigned roles |
| POST | `/api/users/{userId}/roles` | Assign role to user |
| DELETE | `/api/users/{userId}/roles/{roleId}` | Revoke role from user |

---

### League Types — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/league-types` | User | List league types (paginated) |
| GET | `/api/league-types/{id}` | User | Get by ID |
| POST | `/api/league-types` | Admin | Create (e.g. IPL, WPL) |
| PUT | `/api/league-types/{id}` | Admin | Update |
| DELETE | `/api/league-types/{id}` | Admin | Soft-delete |

---

### Leagues — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/leagues` | User | List leagues, optional `?leagueTypeId=` filter |
| GET | `/api/leagues/{id}` | User | Get by ID |
| POST | `/api/leagues` | Admin | Create (e.g. IPL 2025) |
| PUT | `/api/leagues/{id}` | Admin | Update |
| PATCH | `/api/leagues/{id}/status` | Admin | Transition status (UPCOMING→ACTIVE→CLOSED) |
| DELETE | `/api/leagues/{id}` | Admin | Soft-delete |

---

### League Seasons — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/leagues/{leagueId}/seasons` | User | List seasons for a league |
| GET | `/api/seasons/{id}` | User | Get season by ID |
| POST | `/api/leagues/{leagueId}/seasons` | Admin | Create season (e.g. Group Stage) |
| PUT | `/api/seasons/{id}` | Admin | Update |
| PATCH | `/api/seasons/{id}/status` | Admin | Transition status |
| DELETE | `/api/seasons/{id}` | Admin | Soft-delete |

---

### Teams — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/teams` | User | List teams, optional `?name=` filter |
| GET | `/api/teams/{id}` | User | Get by ID |
| POST | `/api/teams` | Admin | Create team |
| PUT | `/api/teams/{id}` | Admin | Update |
| DELETE | `/api/teams/{id}` | Admin | Soft-delete |

### Season Teams — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/seasons/{seasonId}/teams` | User | List teams in a season |
| POST | `/api/seasons/{seasonId}/teams?teamId=` | Admin | Add team to season |
| DELETE | `/api/seasons/{seasonId}/teams/{seasonTeamId}` | Admin | Remove team from season |

---

### Players — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/teams/{teamId}/players` | User | List players for a team |
| GET | `/api/players/{id}` | User | Get player by ID |
| POST | `/api/teams/{teamId}/players` | Admin | Add player to team |
| PUT | `/api/players/{id}` | Admin | Update player |
| DELETE | `/api/players/{id}` | Admin | Soft-delete |

---

### Matches — Admin Write, User Read

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/seasons/{seasonId}/matches` | User | List matches, optional `?status=` filter |
| GET | `/api/matches/{id}` | User | Get match by ID |
| POST | `/api/seasons/{seasonId}/matches` | Admin | Schedule match (lock time auto-computed) |
| PUT | `/api/matches/{id}` | Admin | Update match |
| DELETE | `/api/matches/{id}` | Admin | Soft-delete |

---

### Match Results — Admin Workflow

All write endpoints require `?adminId=` query param.

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/matches/{matchId}/result` | User | Get result (if entered) |
| POST | `/api/matches/{matchId}/result` | Admin | Enter result |
| PUT | `/api/matches/{matchId}/result` | Admin | Update result |
| PATCH | `/api/matches/{matchId}/result/verify` | Admin | Verify (required before publish) |
| PATCH | `/api/matches/{matchId}/result/publish` | Admin | Publish → triggers async scoring |

---

### Match Predictions — Users

Locked after `prediction_lock_time = scheduledAt - 1 hour`.

| Method | Path | Description |
|---|---|---|
| GET | `/api/matches/{matchId}/predictions/my?userId=` | Get own prediction |
| GET | `/api/matches/{matchId}/predictions` | List all (visible after lock closes) |
| POST | `/api/matches/{matchId}/predictions?userId=` | Submit prediction |
| PUT | `/api/matches/{matchId}/predictions/{predictionId}?userId=` | Update prediction |

Prediction body:
```json
{
  "predictedWinnerTeamId":     "<uuid>",
  "predictedTossWinnerTeamId": "<uuid>",
  "predictedPlayerOfMatchId":  "<uuid>"
}
```

---

### League Predictions — Users

Locked after `leaguePredictionLockTime` on the season (default 4 hours before first match).

| Method | Path | Description |
|---|---|---|
| GET | `/api/seasons/{seasonId}/league-predictions/my?userId=` | Get own prediction |
| GET | `/api/seasons/{seasonId}/league-predictions` | List all (visible after lock closes) |
| POST | `/api/seasons/{seasonId}/league-predictions?userId=` | Submit full team ranking |
| PUT | `/api/seasons/{seasonId}/league-predictions/{predictionId}?userId=` | Update ranking |

Prediction body:
```json
{
  "teamRankings": [
    { "teamId": "<uuid>", "predictedRank": 1 },
    { "teamId": "<uuid>", "predictedRank": 2 }
  ]
}
```

---

### League Results — Admin Workflow

All write endpoints require `?adminId=` query param.

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/seasons/{seasonId}/result` | User | Get result (if published) |
| POST | `/api/seasons/{seasonId}/result` | Admin | Enter final standings |
| PUT | `/api/seasons/{seasonId}/result` | Admin | Update standings |
| PATCH | `/api/seasons/{seasonId}/result/verify` | Admin | Verify |
| PATCH | `/api/seasons/{seasonId}/result/publish` | Admin | Publish → triggers league scoring |

---

### Leaderboard — Users

| Method | Path | Description |
|---|---|---|
| GET | `/api/seasons/{seasonId}/leaderboard` | Current standings (latest snapshot) |
| GET | `/api/seasons/{seasonId}/leaderboard/history` | All snapshots (paginated) |

---

### Prediction Points — Users

| Method | Path | Description |
|---|---|---|
| GET | `/api/seasons/{seasonId}/points?userId=&matchId=` | User's points for a season |
| GET | `/api/matches/{matchId}/points` | All points for a match |

---

### Admin-Only Logs

| Method | Path | Description |
|---|---|---|
| GET | `/api/audit-logs` | Audit trail, optional `?entityType=&entityId=` |
| GET | `/api/email-logs` | Email send history, optional `?userId=&eventType=` |
| GET | `/api/email-logs/{id}` | Get single email log |
| GET | `/api/bulk-communications` | List bulk campaigns |
| GET | `/api/bulk-communications/{id}` | Get campaign by ID |
| POST | `/api/bulk-communications?adminId=` | Launch bulk email campaign |

---

## End-to-End Flow

### Admin Setup
```
1. Create league type        POST /api/league-types
2. Create teams              POST /api/teams  (repeat per team)
3. Add players to teams      POST /api/teams/{teamId}/players
4. Create league             POST /api/leagues
5. Create season             POST /api/leagues/{leagueId}/seasons
6. Add teams to season       POST /api/seasons/{seasonId}/teams?teamId=
7. Schedule matches          POST /api/seasons/{seasonId}/matches  (repeat per match)
8. Activate season           PATCH /api/seasons/{seasonId}/status  { "status": "ACTIVE" }
```

### User Participation
```
1. Register                  POST /api/auth/register
2. Login → get token         POST /api/auth/login
3. Join season               POST /api/seasons/{seasonId}/members?userId=
4. Submit league prediction  POST /api/seasons/{seasonId}/league-predictions?userId=
                             (must be before leaguePredictionLockTime)
5. Submit match predictions  POST /api/matches/{matchId}/predictions?userId=
                             (must be before each match's prediction_lock_time)
```

### Per-Match Result Flow (Admin)
```
1. Enter result    POST  /api/matches/{matchId}/result?adminId=
2. Verify          PATCH /api/matches/{matchId}/result/verify?adminId=
3. Publish         PATCH /api/matches/{matchId}/result/publish?adminId=
                   → Async: scores predictions → updates leaderboard
```

### End of Season
```
1. Enter league result   POST  /api/seasons/{seasonId}/result?adminId=
2. Verify                PATCH /api/seasons/{seasonId}/result/verify?adminId=
3. Publish               PATCH /api/seasons/{seasonId}/result/publish?adminId=
                         → Async: scores league predictions → final leaderboard
```

---

## Prediction Lock Rules

| Prediction type | Locks at | Configured by |
|---|---|---|
| Match prediction | `scheduledAt - 1 hour` | `familyleague.prediction.match-lock-hours` |
| League prediction | `season.leaguePredictionLockTime` | `familyleague.prediction.league-lock-hours` |

After the lock time, `PredictionLockedException` is thrown → HTTP 422.

---

## Scoring Engine

- Triggered **asynchronously** on result publish (non-blocking)
- Runs in a dedicated thread pool (`taskExecutor`)
- Calculates points per prediction field (winner, toss winner, player of match)
- Recalculates leaderboard snapshot after every publish
- League ranking predictions scored only after league result is published

Thread pool config:
```properties
familyleague.async.core-pool-size=4
familyleague.async.max-pool-size=8
familyleague.async.queue-capacity=100
```

---

## Key Configuration

```properties
# Server
server.port=9090

# JWT
familyleague.jwt.secret=<base64-encoded-32-byte-key>
familyleague.jwt.expiration-ms=86400000       # 24 hours

# Prediction locks
familyleague.prediction.match-lock-hours=1
familyleague.prediction.league-lock-hours=4

# Email
familyleague.mail.from=noreply@familyleague.com
familyleague.notification.reminder-before-hours=2

# Async scoring
familyleague.async.core-pool-size=4
familyleague.async.max-pool-size=8
familyleague.async.queue-capacity=100
```

---

## Project Structure

```
src/main/resources/
├── application.properties
└── db/
    ├── schema.sql              # DDL — all table definitions
    ├── seed.sql                # Reference data (teams, players, season)
    ├── seed_matches.sql        # Match schedule
    └── erd/
        └── family_league_erd.md  # Entity Relationship Diagram

src/main/java/com/divami/java_project/
├── config/
│   ├── AsyncConfig.java            # Thread pool for scoring engine
│   ├── OpenApiConfig.java          # Swagger / Pageable customization
│   └── SecurityConfig.java         # JWT filter chain, route permissions
├── controller/                     # REST controllers (one per domain)
├── exception/
│   ├── GlobalExceptionHandler.java # Maps all exceptions to HTTP responses
│   ├── BusinessException.java
│   ├── DuplicateResourceException.java
│   ├── PredictionLockedException.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── base/BaseAuditEntity.java   # created_at, updated_at, deleted_at
│   └── dto/                        # Request/response DTOs (Java records)
├── repository/                     # Spring Data JPA repositories
├── scheduler/
│   └── NotificationScheduler.java  # Hourly prediction reminder emails
├── security/
│   ├── JwtAuthFilter.java          # OncePerRequestFilter JWT validation
│   ├── JwtProvider.java            # Token generation and parsing
│   └── UserDetailsServiceImpl.java # Loads user + roles from DB
└── service/
    └── impl/                       # All service implementations
```

---

## Error Responses

All errors return a consistent JSON body:

```json
{
  "code": "PREDICTION_LOCKED",
  "message": "Prediction window is closed for this match",
  "timestamp": "2025-03-22T09:00:00Z"
}
```

| HTTP Status | Code | When |
|---|---|---|
| 400 | `VALIDATION_ERROR` | `@Valid` constraint violation |
| 400 | `BUSINESS_ERROR` | Business rule violation |
| 400 | `INVALID_PARAMETER` | Bad sort field or query param |
| 404 | `NOT_FOUND` | Entity does not exist |
| 409 | `DUPLICATE_RESOURCE` | Unique constraint violation |
| 422 | `PREDICTION_LOCKED` | Past prediction lock time |
| 500 | `INTERNAL_ERROR` | Unexpected server error |

---

## Production Checklist

- [ ] Change `familyleague.jwt.secret` to a new random 32-byte Base64 key
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Configure real SMTP credentials
- [ ] Set up HTTPS via reverse proxy (Nginx / Caddy)
- [ ] Schedule regular PostgreSQL backups
- [ ] Review `familyleague.async` thread pool sizing under expected load
