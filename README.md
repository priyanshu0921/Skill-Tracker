# SkillTracker

`SkillTracker` is a full-stack skill tracking application built with `Spring Boot`, `React`, and `PostgreSQL`.
It was adapted from your original Node/Express skill tracker and now includes:

- JWT-based authentication
- full CRUD for skills and goals
- assessments and training recommendations
- personalized roadmap generation with progress tracking and CRUD
- PostgreSQL configuration for `SkillTrackerDB`

## Project Structure

```text
  SkillTracker/
  backend/   Spring Boot 3 API
  frontend/  React + Vite client
```

## Backend Stack

- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL

## Frontend Stack

- React 18
- Vite
- Fetch-based API integration

## PostgreSQL Configuration

The backend is already configured for your database details in
[application.yml](/Users/sumit/Documents/New%20project/SkillTracker/backend/src/main/resources/application.yml).

- Database: `SkillTrackerDB`
- Username: blank
- Password: blank
- URL: `jdbc:postgresql://localhost:5432/SkillTrackerDB`

This matches the setup you described for DBeaver.

## Main API Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/dashboard`
- `GET /api/skills`
- `POST /api/skills`
- `PUT /api/skills/{skillId}`
- `DELETE /api/skills/{skillId}`
- `GET /api/goals`
- `POST /api/goals`
- `PUT /api/goals/{goalId}`
- `DELETE /api/goals/{goalId}`
- `GET /api/roadmaps`
- `POST /api/roadmaps`
- `PUT /api/roadmaps/{roadmapId}`
- `DELETE /api/roadmaps/{roadmapId}`
- `PATCH /api/roadmaps/{roadmapId}/steps/{stepId}/toggle`
- `GET /api/assessments`
- `POST /api/assessments/{id}/submit`
- `GET /api/training`

## Run the Project

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Notes

- The backend seeds a demo user:
  - Email: `demo@skilltracker.dev`
  - Password: `password123`
- The backend also seeds starter skills, goals, assessments, training resources, and a roadmap for the demo user.
- Roadmaps are now persisted in PostgreSQL instead of Firebase/Mongo-style storage.
- The application was verified with:
  - backend compile: `mvn -q -DskipTests compile`
  - frontend production build: `npm run build`
<<<<<<< HEAD
=======
# Skill-Tracker
# Skill-Tracker
>>>>>>> 09c4eee92be45cfc5946ff540480fe680c4f2726
# Skill-Tracker
