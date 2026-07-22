# Job Application Tracker

A full-stack app to track job applications — built with Spring Boot + MySQL (backend) and React + Tailwind (frontend). JWT-based authentication.

🔗 **Live demo:** https://job-tracker-red-beta.vercel.app

## Features
- Sign up / log in with JWT auth (BCrypt-hashed passwords)
- Add, edit, delete applications
- Filter by status, search by company name
- Pagination
- Export all applications to CSV
- Daily email reminders for applications sitting in "Applied" status too long
- Status breakdown pie chart

## Tech stack
- **Backend:** Spring Boot 3, Spring Security, Spring Data JPA, MySQL, JWT
- **Frontend:** React, Vite, Tailwind CSS, Recharts, Axios
- **Deployment:** Railway (backend + MySQL), Vercel (frontend)

## Project structure
\```
job-tracker/
├── backend/    Spring Boot REST API
└── frontend/   React + Vite app
\```

## Prerequisites
- Java 17+
- Maven (or use VS Code's built-in Maven support)
- Node.js 18+
- MySQL running locally

## Backend setup
1. Open `backend/` in VS Code (install "Extension Pack for Java" and "Spring Boot Extension Pack").
2. Edit `backend/src/main/resources/application.properties` or set environment variables — see `.env.example` style below.
3. Run: `cd backend && mvn spring-boot:run`
4. Backend runs on **http://localhost:8080**

## Frontend setup
1. `cd frontend && npm install && npm run dev`
2. Frontend runs on **http://localhost:5173**

## Environment variables (for deployment)
Backend (Railway): DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET, CORS_ORIGIN, MAIL_USERNAME, MAIL_PASSWORD, REMINDER_DAYS

Frontend (Vercel): VITE_API_URL=https://your-backend-url/api

## How auth works (for interview prep)
1. On signup/login, the backend generates a signed JWT and returns it.
2. The frontend stores the token in `localStorage` and attaches it to every request via an axios interceptor.
3. `JwtAuthFilter` validates the token on each request and tells Spring Security who the user is — stateless, no server-side sessions.
4. Passwords are hashed with BCrypt before storage.

## Known simplifications (worth understanding, not hiding)
- No refresh tokens — JWT expires after 24h, user re-logs in.
- `userId` on `JobApplication` is a plain foreign key rather than a full JPA `@ManyToOne` relationship.
- The stats pie chart reflects the current page of results, not the full history, since the dashboard paginates. A dedicated `/api/applications/stats` endpoint with a `COUNT... GROUP BY status` query would be the production fix.
- Reminder emails use plain-text `SimpleMailMessage` rather than HTML templates.

## Possible next features
- Sort applications by date/company
- Bulk actions (mark multiple as rejected, etc.)
- Dark mode
- Refresh tokens
