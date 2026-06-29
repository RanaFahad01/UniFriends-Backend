# UniFriends (Backend)

Spring Boot REST API and WebSocket server for [UniFriends](https://www.unifriends.net), a social platform for university students.

**Live → [api.unifriends.net](https://api.unifriends.net)** · Frontend repo: [unifriends-frontend](https://github.com/RanaFahad01/UniFriends-Frontend)

---

## Tech stack

| Concern | Technology |
|---|---|
| Framework | Spring Boot 3.5, Java 21 |
| Security | Spring Security, Google OAuth2, JWT |
| Database | PostgreSQL 18, Spring Data JPA, Flyway |
| Real-time | STOMP over SockJS (Spring WebSocket) |
| Hosting | Railway |

---

## Running locally

**Prerequisites:** Java 21, PostgreSQL

```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
# Fill in: DB credentials, JWT_SECRET, Google OAuth client ID/secret, profanity API key
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The `application-local.yaml.example` documents every required value. The real file is gitignored and never committed (important if you want your credentials to be safe from crawlers and baddies! Make sure to rotate secrets if they've been accidentally committed).

---

## Security design

**JWT in httpOnly cookie**

After Google OAuth2 completes, the backend issues two cookies: a `jwt` cookie (`httpOnly`, `Secure`, `SameSite=Lax`) carrying the signed token, and a `jwt_present` flag cookie readable by JavaScript to detect session state. The token itself is never exposed to the browser's JS runtime, eliminating XSS-based token theft.

**WebSocket authentication**

Browsers cannot send `Authorization` headers on WebSocket connections, and putting a JWT in the URL leaks it to proxy and server access logs. Instead, the client first calls `GET /api/auth/ws-ticket` (a normal cookie-authenticated REST endpoint) to receive a short-lived one-time UUID (30s TTL, stored in a `ConcurrentHashMap`). That ticket is presented on the STOMP connect frame and consumed immediately.

**Security filter chain**

Every request passes through:
1. `JwtAuthFilter`: extracts and validates the JWT from the cookie, populates the `SecurityContext`
2. `BannedUserFilter`: rejects banned users with `403` before they reach any controller, regardless of JWT validity

**CORS**

`Access-Control-Allow-Origin` is locked to the configured frontend domain. No wildcard. Credentials mode is enabled for cookie transport.

**Role-based access control**

Three roles: `USER`, `MODERATOR`, `ADMIN`. They are enforced at the controller level via Spring Security method security. Moderators can delete posts and ban users. Admins can assign and revoke the moderator role.

---

## Database

12 Flyway migrations manage the schema. Key tables: `users`, `leagues`, `league_members`, `posts`, `chat_messages`, `reports`.

Schema changes are always additive migrations, which means no destructive `ALTER` happens without a migration file, so the deployed schema and entity model stay in sync.

---

## Project structure

```
src/main/java/com/ranafahad/unifriends/
  auth/         JWT filter, banned user filter, OAuth2 success handler, WS ticket store
  config/       Security, CORS, WebSocket configuration
  user/         User entity, repository, service, controller
  post/         Post entity, service, controller, profanity check
  league/       League entity, membership, join request handling
  chat/         WebSocket message broker, chat message persistence
  report/       Moderation reports
  common/       Global exception handler, shared DTOs
```
