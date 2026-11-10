# Robo Dance

Zumba-style fitness app built as a Spring Cloud microservices project (Config Server, Eureka, Gateway, Auth, Workouts, Progress) with a React frontend, Docker, and Kubernetes.

See [ROADMAP.md](./ROADMAP.md) for phases and features.

## Phase 1 — Foundation (current)

| Piece | Port | Role |
|-------|------|------|
| Config Server | `8071` | Central config (`native` classpath) |
| Eureka Server | `8070` | Service discovery |
| MySQL (Docker) | `3307` | DBs: `robodance_auth`, `robodance_workouts`, `robodance_progress` (host `3306` is your local MySQL) |

### Prerequisites

- JDK 17, Maven 3.9+, Docker Desktop

### Start MySQL

```bash
cd docker-compose
docker compose up -d
```

### Start Config Server

```bash
cd backend/configserver
mvn spring-boot:run
```

### Start Eureka

```bash
cd backend/eurekaserver
mvn spring-boot:run
```

Then open:

- Eureka dashboard: http://localhost:8070
- Config example: http://localhost:8071/eurekaserver/default

### Conventions

| Item | Value |
|------|--------|
| Java package root | `com.robodance` |
| JWT secret (dev) | via Config Server `application.yml` |
| MySQL user | `robodance` / `robodance` |

### Phase 2 ports

| Service | Port |
|---------|------|
| API Gateway | `8072` |
| auth-service | `8081` |
| workouts-service | `8082` |
| progress-service | `8083` |
| React frontend | `5173` (Phase 3) |

### Start order (Phase 2)

1. MySQL (`docker compose up -d`)
2. Config Server
3. Eureka
4. auth-service, workouts-service, progress-service
5. Gateway

### Smoke test via Gateway

```bash
# register
curl -X POST http://localhost:8072/api/auth/register -H "Content-Type: application/json" -d "{\"email\":\"dancer@robo.test\",\"displayName\":\"Dancer\",\"password\":\"secret12\"}"

# login, then use token:
curl http://localhost:8072/api/workouts?level=BEGINNER -H "Authorization: Bearer TOKEN"
curl http://localhost:8072/api/progress -H "Authorization: Bearer TOKEN"
```
