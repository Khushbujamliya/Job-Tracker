# Job Application Tracker

A full-stack app to track your job applications — built with Spring Boot + MySQL (backend) and React + Tailwind (frontend). JWT-based authentication.

## Project structure
```
job-tracker/
├── backend/    Spring Boot REST API
└── frontend/   React + Vite app
```

## Prerequisites
- Java 17+
- Maven (or use VS Code's built-in Maven support)
- Node.js 18+
- MySQL running locally

## Backend setup

1. Open `backend/` in VS Code (install the "Extension Pack for Java" and "Spring Boot Extension Pack" if you haven't).
2. Create a MySQL database (or let the app create it — see below).
3. Edit `backend/src/main/resources/application.properties`:
   - Set `spring.datasource.password` to your actual MySQL root password.
   - `createDatabaseIfNotExist=true` in the URL means MySQL will auto-create the `job_tracker` database on first run — you don't need to create it manually.
4. Run it:
   - In VS Code: open `TrackerApplication.java` → click "Run" above the `main` method.
   - Or from terminal: `cd backend && mvn spring-boot:run`
5. Backend runs on **http://localhost:8080**

Test it's working: `POST http://localhost:8080/api/auth/signup` with body:
```json
{ "name": "Test User", "email": "test@test.com", "password": "test123" }
```
(Use Postman or VS Code's "Thunder Client" extension for this.)

## Frontend setup

1. Open a new terminal:
   ```
   cd frontend
   npm install
   npm run dev
   ```
2. Frontend runs on **http://localhost:5173**
3. Sign up, log in, and start adding applications.

## How auth works (for interview prep)
1. On signup/login, the backend generates a signed JWT and sends it back.
2. The frontend stores the token in `localStorage` and attaches it to every API request via an axios interceptor (`src/api/axios.js`).
3. On the backend, `JwtAuthFilter` intercepts every request, validates the token, and tells Spring Security who the user is — without needing server-side sessions (stateless auth).
4. Passwords are never stored in plain text — they're hashed with BCrypt before saving.

## Known simplifications (worth understanding, not hiding)
- No refresh tokens — JWT just expires after 24h and the user re-logs in. A real production app would add refresh tokens.
- `userId` on `JobApplication` is a plain foreign key, not a full JPA `@ManyToOne` relationship — kept simple on purpose, but you should be able to explain what a `@ManyToOne` version would look like if asked.
- No pagination on the applications list — fine at small scale, but worth mentioning as a next step if asked "how would you scale this?"

## Suggested next features (good talking points once built)
- Filter/search applications by status or company
- Email reminders for follow-ups
- Export to CSV
- Pagination once the list grows
