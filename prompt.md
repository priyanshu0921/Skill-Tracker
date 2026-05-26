## SkillTracker — Producton-Grade Enterprise Learning & Career Development Platorm 

SkillTracker is the first enterprise learning and career development platform.SkillTracker is the Pioneer Enterprise Learning & Career Development Platform. 

## Context & Role 

You are a senior enterprise software engineer/architect proficient in building scalable learning ecosystems, secure backend systems, analytics dashboards and production-grade full stack applications. 

You are not creating a tutorial project, toy application, mock-up, or partial prototype of a SkillTracker platform, but rather an entire enterprise grade platform. 

This system should be a real world deployable learning and career development system that is utilized by: 

- enterprises 

- universities 

- HR departments 

- bootcamps 

- professionals 

- distributed teams 

The platform should emulate a contemporary smart learning environment that is able to accommodate: 

- Several thousands of simultaneous users 

- scalable modular architecture 

- provide security for authentication and authorization 

- interactive dashboard analytics 

- personalized learning systems 

- real-time progress tracking 

- enterprise-grade maintainability 

- cloud-native deployments 

The application should enable the users to: 

- Monitor development and mastery of skills 

- Organise own learning objectives 

- Create individual learning pathways 

- complete assessments and quizzes 

- receive AI-powered recommendations 

- Visualize learning progress in the analysis of learning progress. 

- Keep an eye on the job market and career prospects. 

- manage certifications and achievements 

- The final product will be: 

- production-grade 

- enterprise-ready 

- fully runnable 

- scalable 

- maintainable 

- secure 

- accessible 

- responsive 

- optimized for performance 

- modular and extensible 

All APIs, services, DTOs, entities, components, configurations, hooks, and routes need to have some code that is executable. 

## Fixed Technology Stack 

## Frontend 

React 18 

Vite 

Tailwind CSS 

TypeScript 

React Router DOM 

Axios 

React Query 

React Hook Form 

Zod 

Framer Motion Recharts Lucide React 

## Backend 

Java 21 Spring Boot 3 Spring Security Spring Data JPA PostgreSQL JWT Authentication Flyway Migration Hibernate Validator Lombok 

## DevOps & Infrastructure 

Docker 

Docker Compose GitHub Actions CI/CD Swagger/OpenAPI Vercel / Netlify 

Railway / Render / AWS PostgreSQL Cloud Hosting 

## High-Level System Architecture 

The system should include: 

Frontend (React + Vite + Tailwind) 

│ 

│ HTTPS + JWT 

**==> picture [34 x 9] intentionally omitted <==**

Spring Boot REST API 

│ 

│ JPA/Hibernate 

▼ 

PostgreSQL Database 

Architecture should adhere to the clean separation of concerns. 

Business logic should only be placed in services. 

Controllers should only: 

- validate requests 

- invoke services 

- return structured responses 

## Project Structure Requirements 

## Frontend Structure 

frontend/ 

│ 

`├` ── src/ 

- │ `├` ── components/ 

- │   │ `├` ── dashboard/ 

- │   │ `├` ── skills/ 

- │   │ `├` ── goals/ 

- │   │ `├` ── roadmap/ 

- │   │ `├` ── assessments/ 

- │   │ `├` ── recommendations/ 

│   │ `├` ── charts/ 

│   │   └── common/ 

│   │ 

│ `├` ── pages/ 

│   │ `├` ── auth/ 

│   │ `├` ── dashboard/ 

│   │ `├` ── skills/ 

│   │ `├` ── goals/ 

│   │ `├` ── roadmap/ 

│   │ `├` ── assessments/ 

│   │ `├` ── recommendations/ 

│   │   └── profile/ 

│   │ 

│ `├` ── hooks/ 

│ `├` ── services/ 

│ `├` ── context/ 

│ `├` ── routes/ 

│ `├` ── layouts/ 

│ `├` ── store/ 

│ `├` ── utils/ 

│ `├` ── styles/ 

│ `├` ── assets/ 

│   └── main.tsx │ 

`├` ── public/ 

`├` ── package.json 

`├` ── vite.config.ts 

`├` ── Dockerfile 

└── README.md 

## Backend Structure 

backend/ 

│ 

`├` ── src/main/java/com/skilltracker/ 

│ `├` ── config/ 

│ `├` ── controllers/ 

│ `├` ── dto/ 

│ `├` ── entities/ 

│ `├` ── enums/ 

│ `├` ── exceptions/ 

│ `├` ── repositories/ 

│ `├` ── security/ 

│ `├` ── services/ 

│ `├` ── validation/ 

│ `├` ── utils/ 

│   └── SkillTrackerApplication.java 

│ 

`├` ── src/main/resources/ 

- │ `├` ── application.yml 

│   └── db/migration/ 

│ 

`├` ── src/test/ 

`├` ── pom.xml 

`├` ── Dockerfile 

└── README.md 

## Authentcaton & Authorizaton System 

Develop a production-ready JWT authentication system. 

## Required Features 

- User Registration 

- User Login 

- Secure Logout 

- Protected Routes 

- Route Guards 

- Session Persistence 

- Token Refresh Flow 

- JWT Expiration Validation 

- Auto Login Persistence 

- Role-Based Authorization 

- Multi-role support 

- Secure password recovery architecture 

## Security Requirements 

- Use Spring Security 

- To encrypt passwords use BCrypt. 

- Do not store plain text passwords!Avoid storing clear text passwords! 

- Check the validity of JWT signature and expiration. 

- Seek to get secrets from environment variables. 

- Block unauthorised API access. 

- Add rate limiting to requests. 

- Add brute-force protection 

- CAPTCHA-ready login flow 

Set a refresh token expiration.Set refresh token expiration. 

If applicable, enable support for cookies via HTTP only. 

## User Roles 

ADMIN 

USER 

MODERATOR 

## Database Design Requirements 

Take advantage of PostgreSQL, enterprise-class relational database design. 

Always use UUIDs or BIGSERIAL primary keys. 

Set up correct indexing of all searchable fields. 

Don't use automatic schema generation, use Flyway migrations. 

## Database Tables 

## users 

id 

full_name 

email 

password 

role 

avatar_url 

bio 

is_active 

created_at 

updated_at 

## skills 

id 

user_id 

name 

category 

proficiency 

priority 

progress 

created_at 

updated_at 

## goals 

id 

user_id 

title 

description 

deadline 

priority 

status 

progress 

created_at 

updated_at 

## roadmaps 

id 

user_id 

title 

description 

progress 

created_at 

updated_at 

## roadmap_steps 

id 

roadmap_id 

title 

description 

completed 

step_order 

created_at 

## assessments 

id 

user_id 

skill_name 

score 

total_questions 

result 

completed_at 

## recommendatons 

id 

user_id 

type 

title 

url 

reason 

created_at 

## notfcatons 

id 

user_id 

title 

message 

is_read 

created_at 

## Database Requirements 

The database layer should contain: 

- proper foreign keys 

- cascade delete/update rules 

- indexed columns 

- validation constraints 

- optimized joins 

- pagination support 

- transaction-safe operations 

- DTO projections 

- audit-ready timestamps 

## Dashboard Requirements 

Create an analytics dashboard that's similar to an enterprise productivity platform. 

## Dashboard Features 

- Skill analytics 

- Goal completion tracking 

- Roadmap progression 

- Assessment statistics 

- Recommendation feed 

- Learning trends 

- Weekly productivity metrics 

- Achievement tracking 

- Activity timeline 

## UI Requirements 

Incorporate a contemporary high quality user interface with: 

- glassmorphism cards 

- soft gradients 

- responsive layouts 

- smooth animations 

- hover interactions 

- skeleton loaders 

- animated charts 

- progress indicators 

- modern typography 

- dark/light mode 

- mobile responsiveness 

## Skills Management System 

Users should be capable of: 

- create skills 

- edit skills 

- delete skills 

- categorize skills 

- set proficiency levels 

- prioritize learning 

- search skills 

- filter by category 

## UI Features 

- animated progress bars 

- drag-and-drop prioritization 

- real-time updates 

- debounced search 

- dynamic filtering 

## **Goals Management System** 

Add a comprehensive goal tracking system.Add a full goal tracking system. 

## Features 

- goal creation 

- goal editing 

- progress tracking 

- deadline management 

- completion tracking 

- status indicators 

- priority tagging 

- analytics visualization 

- reminder-ready architecture 

## Personalized Roadmap Engine 

The roadmap system should: 

- generate learning paths 

- identify skill gaps 

- recommend learning sequences 

- track completion percentages 

- Create, Read, Update and Delete routes on the support map. 

- Support CRUD operations for the support roadmap 

## Roadmap UI 

- timeline visualization 

- expandable cards 

- milestone indicators 

- progress calculation 

- step completion toggles 

## Assessment System 

Develop a production-ready assessment engine. 

## Features 

- multiple quizzes 

- dynamic assessments 

- score calculation 

- skill gap analysis 

- performance tracking 

- assessment history 

- analytics dashboards 

- recommendation generation 

## AI Recommendaton Engine 

Generate recommendations for: 

- courses 

- tutorials 

- certifications 

- projects 

- documentation 

- learning resources 

## Recommendaton Logic 

Recommendations should use: 

- assessment scores 

- user goals 

- roadmap progress 

- skill proficiency 

- weak skill analysis 

- learning activity trends 

## API Requirements 

Write RESTful APIs with proper validation and structured response. 

## Authentcaton APIs 

POST /api/auth/register 

POST /api/auth/login 

POST /api/auth/logout 

POST /api/auth/refresh 

GET  /api/auth/me 

## Skills APIs 

GET    /api/skills POST   /api/skills PUT    /api/skills/{id} DELETE /api/skills/{id} 

## Goals APIs 

GET    /api/goals POST   /api/goals PUT    /api/goals/{id} DELETE /api/goals/{id} 

## Roadmap APIs 

GET    /api/roadmaps POST   /api/roadmaps 

PUT    /api/roadmaps/{id} 

DELETE /api/roadmaps/{id} 

## Assessment APIs 

GET  /api/assessments 

POST /api/assessments/submit 

## Dashboard APIs 

GET /api/dashboard/analytics 

GET /api/dashboard/progress 

GET /api/dashboard/recommendations 

## Notfcaton APIs 

GET /api/notifications 

PUT /api/notifications/{id}/read 

## API Response Structure 

## Success Response 

{ 

"success": true, 

Operation completed successfully": 

"data": {} 

} 

## Error Response 

{ 

"success": false, 

"message": "Validation failed", 

"errors": [] 

} 

## Error Handling Requirements 

Each domain error should include: 

- dedicated exception class 

- HTTP status code 

- structured JSON response 

- meaningful validation message 

## ons Required Except 

AuthenticationException 

InvalidCredentialsException 

TokenExpiredException 

AccessDeniedException 

ResourceNotFoundException 

ValidationException 

DatabaseException 

RateLimitException 

## Global Excepton Handling 

Implement: 

- @RestControllerAdvice 

- validation error mapping 

- JWT exception handling 

- database constraint handling 

- Standard API error structure (including API error code, error message, and error object). 

Avoid sharing stack traces or information from sensitive areas. 

## Security Requirements 

Reserve the platform from: 

- SQL injection 

- XSS attacks 

- CSRF attacks 

- unauthorized API access 

- token hijacking 

- Additional Security Features 

- request sanitization 

- input validation 

- secure headers 

- CSP policies 

- audit logging 

- secure environment variables 

- rate limiting 

- role-based access control 

## Accessibility Requirements 

The platform will be compliant with accessibility standards of WCAG. 

## Required Accessibility Features 

- semantic HTML 

- ARIA labels 

- keyboard navigation 

- focus management 

- screen reader support 

- accessible forms 

- contrast compliance 

- responsive scaling 

## Frontend Performance Requirements 

Implement: 

- lazy loading 

- code splitting 

- memoization 

- virtualization 

- optimized rendering 

- image optimization 

- React Query caching 

- debounced search 

- efficient state management 

## Backend Performance Requirements 

Implement: 

- pagination 

- query optimization 

- database indexing 

- DTO projections 

- async processing 

- connection pooling 

- caching strategy 

- optimized joins 

- 

## Testng Requirements 

Provide detailed tests for all meaningful functionality. 

## Backend Testng 

Use: 

- JUnit 5 

- Mockito 

- Spring Boot Test 

Test: 

- authentication flows 

- JWT validation 

- service logic 

- repository queries 

- API endpoints 

- validation rules 

- exception handling 

## Frontend Testng 

Use: 

- Vitest 

- React Testing Library 

Test: 

- components 

- hooks 

- forms 

- route protection 

- API integrations 

- dashboard rendering 

## End-to-End Testng 

Use Cypress. 

Test: 

- authentication flow 

- CRUD workflows 

- dashboard functionality 

- protected routes 

- assessment submissions 

## Load & Performance Testng 

Simulate: 

- concurrent API requests 

- The `render()` function is used for dashboard.For dashboard, use function `render()`. 

- multiple authenticated users 

- large dataset pagination 

## Monitoring & Logging 

Implement: 

- Spring Boot Actuator 

- structured logging 

- request tracing 

- health checks 

- performance monitoring 

- centralized logging 

- audit-ready event tracking 

## Docker & Deployment Requirements 

Complete deployment ready setup. 

## Required Files 

Dockerfile 

docker-compose.yml 

.env.example 

.github/workflows/ 

## Frontend Deployment 

Support: 

- Vercel 

- Netlify 

## Backend Deployment 

Support: 

- Railway 

- Render 

- AWS 

## Database Deployment 

Support: 

- Neon 

- Supabase 

- Railway PostgreSQL 

- AWS RDS 

## Documentaton Requirements 

Generate complete documentation. 

## Required Documentaton 

- README.md 

- setup guide 

- API documentation 

- Swagger/OpenAPI integration 

- deployment guide 

- environment variables guide 

- architecture explanation 

- testing instructions 

- database setup guide 

## Code Quality Requirements 

The implementation must: 

- contain executable code 

- avoid placeholder implementations 

- avoid TODO sections 

- follow clean architecture 

- follow SOLID principles 

- use meaningful naming 

- include proper comments 

- separate concerns correctly 

- implement proper validation 

- use reusable components 

- follow enterprise coding standards 

## What I'm expectng to get out of it. 

A full, a complete, enterprise-grade codebase that I will expect is complete, and is runnable. 

The answer should contain: 

- complete frontend implementation 

- complete backend implementation 

- executable production-ready code 

- JWT authentication system 

- fully functional dashboards 

- CRUD modules 

- Docker configuration 

- deployment configuration 

- database migrations 

- environment setup 

- CI/CD readiness 

- testing setup 

- API documentation 

- setup instructions 

