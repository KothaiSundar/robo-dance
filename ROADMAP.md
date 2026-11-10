# Robo Dance — Roadmap

Build order for the Zumba-style **Robo Dance** microservices app (Spring Boot + React + Docker + Kubernetes).

**Product:** Beginner / Intermediate / Advanced → 30-day plans with workout videos, daily goals, progress tracking, and badges. Invented robot-dance step lists per day; placeholder video URLs in v1.

**Order:** 1 → 2 → 3 → 4 → 5 → 6 (do not skip). Phase 7 is optional after v1.

---

## Core features (in scope)

| Feature | What it means |
|---------|----------------|
| **Sign up / Log in** | Personal fitness account (register + JWT login) |
| **Choose a Dance Workout** | Beginner, Intermediate, or Advanced |
| **Watch and Dance** | Follow Zumba / Robo Dance workout videos (+ step list) |
| **Set a Daily Goal** | Dance for **10, 20, or 30 minutes** |
| **Track Progress** | Workout history, streaks, estimated calories |
| **Earn Rewards** | Unlock badges for completing workouts |

---

## Phases

| Phase | What | Done when |
|-------|------|-----------|
| **1. Foundation** | Repo layout, Config Server, Eureka, MySQL | Services discover each other; Config + MySQL reachable ✅ |
| **2. APIs** | Auth, Workouts, Progress (goals/streaks/calories/badges), Gateway | Login + workouts + progress/rewards via Gateway ✅ |
| **3. React** | All UI screens through Gateway | Full browser user flow works |
| **4. Docker** | Dockerfiles + Compose | `docker compose up` runs everything |
| **5. Kubernetes** | Deployments, Services, Ingress | Same app on local K8s |
| **6. Polish** | UI + README + demo path | Goal → dance → complete → streak/badge shown |
| **7. Later** | Circuit breakers, tracing, Kafka, CI/CD, mobile | Optional after v1 |

---

## Phase details

### Phase 1 — Foundation
- Repo `robo-dance/` (or this project folder)
- Config Server + Eureka
- MySQL
- Shared conventions (ports, package names, JWT secret via config)

**Done when:** Eureka UI is up; Config Server serves config; MySQL reachable.

### Phase 2 — Microservices + Gateway
- **auth-service** — sign up / log in, JWT, personal fitness account
- **workouts-service** — Beginner / Intermediate / Advanced catalogs; seed 90 invented robo-dance routines + step how-tos + placeholder video URLs
- **progress-service** — daily goal (10 / 20 / 30 min), complete workouts, history, streaks, estimated calories, badges
- **API Gateway** — routes + CORS + JWT at edge

**Done when:** register/login works; workouts return video + `steps[]`; user can set daily goal; complete workout updates history/streak/calories and unlocks badges via Gateway.

### Phase 3 — React frontend
- Screens: Welcome → Sign up / Log in → Choose level → Set daily goal → Calendar / workout list → Watch & dance (video + steps) → Progress (history, streaks, calories) → Rewards (badges)
- All calls go through Gateway only
- Auth token in memory/localStorage

**Done when:** full browser flow works without hitting services directly.

### Phase 4 — Docker
- Dockerfile per service + frontend
- `docker-compose.yml` for MySQL + all backends + gateway + frontend

**Done when:** one command starts the whole local stack.

### Phase 5 — Kubernetes
- Deployments, Services, ConfigMaps/Secrets, Ingress on local cluster

**Done when:** same app reachable via Ingress on K8s.

### Phase 6 — Polish + demo
- Robo Dance UI polish (dark stage, cyan + lime, circuit-style day tiles)
- README + demo script covering all six core features

**Done when:** sign up → pick level → set goal → dance → complete → see streak/calories/badge is documented and reliable.

### Phase 7 — Later (optional)
Circuit breakers, Zipkin/Grafana, Kafka day-completed events, Helm, CI/CD, admin for video URLs, native mobile.

---

## Architecture (target)

| Service | Responsibility |
|---------|----------------|
| configserver | Central config |
| eurekaserver | Service discovery |
| gatewayserver | Routing, CORS, JWT validation |
| auth-service | Sign up / log in, JWT, personal fitness account |
| workouts-service | Levels, 90 workouts, video URLs, nested dance steps |
| progress-service | Daily goal, history, streaks, calories, badges |
| frontend | React (Vite) Robo Dance UI |

**Database-per-service:** separate MySQL schemas for auth, workouts, progress.

### API surface (via Gateway)
- `POST /api/auth/register`, `POST /api/auth/login`
- `GET /api/workouts?level=beginner`
- `GET /api/workouts/{level}/{day}` — includes video URL + `steps[]`
- `GET /api/progress` — history, streak, estimated calories, daily goal
- `PUT /api/progress/level`
- `PUT /api/progress/daily-goal` — `10` | `20` | `30` minutes
- `POST /api/progress/complete`
- `GET /api/progress/badges` — earned + locked rewards

---

## Out of scope for v1 (Phases 1–6)

- Native iOS/Android apps
- Payments / subscriptions
- Live streaming
- Filmed dance videos (text steps are invented; video URLs are placeholders)
- Production cloud / multi-region

## Success criteria (end of Phase 6)

- All microservices register with Eureka and are reachable via Gateway
- Full user flow works on Docker Compose
- Same stack runs on Kubernetes
- All six core features work end-to-end (account, level, video, daily goal, progress, badges)

---

## Tech stack

- Java 17+, Spring Boot 3, Spring Cloud (Config, Eureka, Gateway)
- Spring Security + JWT
- Spring Data JPA + MySQL
- React + Vite + TypeScript + React Router
- Docker + Docker Compose
- Kubernetes

## Content note

You do **not** invent choreography. Phase 2 seeds 90 unique robot-dance routines (step name + counts + short how-to). Videos stay as placeholders until you optionally replace URLs later.
