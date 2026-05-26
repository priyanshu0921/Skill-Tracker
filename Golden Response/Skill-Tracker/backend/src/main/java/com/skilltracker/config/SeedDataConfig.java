package com.skilltracker.config;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.Assessment;
import com.skilltracker.model.AssessmentQuestion;
import com.skilltracker.model.GoalStatus;
import com.skilltracker.model.LearningGoal;
import com.skilltracker.model.ProficiencyLevel;
import com.skilltracker.model.Roadmap;
import com.skilltracker.model.RoadmapResource;
import com.skilltracker.model.RoadmapStep;
import com.skilltracker.model.Role;
import com.skilltracker.model.Skill;
import com.skilltracker.model.TrainingResource;
import com.skilltracker.model.TrainingResourceType;
import com.skilltracker.repository.AppUserRepository;
import com.skilltracker.repository.AssessmentQuestionRepository;
import com.skilltracker.repository.AssessmentRepository;
import com.skilltracker.repository.LearningGoalRepository;
import com.skilltracker.repository.RoadmapRepository;
import com.skilltracker.repository.SkillRepository;
import com.skilltracker.repository.TrainingResourceRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedStarterUser(
            AppUserRepository userRepository,
            SkillRepository skillRepository,
            LearningGoalRepository goalRepository,
            AssessmentRepository assessmentRepository,
            AssessmentQuestionRepository questionRepository,
            RoadmapRepository roadmapRepository,
            TrainingResourceRepository trainingResourceRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.existsByEmailIgnoreCase("demo@skilltracker.dev")) {
                return;
            }

            AppUser user = userRepository.save(AppUser.builder()
                    .fullName("Demo Learner")
                    .email("demo@skilltracker.dev")
                    .passwordHash(passwordEncoder.encode("password123"))
                    .role(Role.USER)
                    .createdAt(Instant.now())
                    .build());

            Skill spring = skillRepository.save(Skill.builder()
                    .user(user)
                    .name("Spring Boot")
                    .category("Backend")
                    .proficiencyLevel(ProficiencyLevel.ADVANCED)
                    .confidenceScore(78)
                    .targetScore(92)
                    .priorityRank(1)
                    .lastPracticedOn(LocalDate.now().minusDays(2))
                    .learningNote("Strong on REST APIs. Focus next on security, caching, and integration testing.")
                    .build());

            Skill react = skillRepository.save(Skill.builder()
                    .user(user)
                    .name("React")
                    .category("Frontend")
                    .proficiencyLevel(ProficiencyLevel.INTERMEDIATE)
                    .confidenceScore(70)
                    .targetScore(88)
                    .priorityRank(2)
                    .lastPracticedOn(LocalDate.now().minusDays(1))
                    .learningNote("Good component fundamentals. Improve data flow decisions and performance patterns.")
                    .build());

            Skill sql = skillRepository.save(Skill.builder()
                    .user(user)
                    .name("SQL")
                    .category("Database")
                    .proficiencyLevel(ProficiencyLevel.INTERMEDIATE)
                    .confidenceScore(64)
                    .targetScore(85)
                    .priorityRank(3)
                    .lastPracticedOn(LocalDate.now().minusDays(6))
                    .learningNote("Practice joins, indexing, and query tuning with larger datasets.")
                    .build());

            goalRepository.save(LearningGoal.builder()
                    .user(user)
                    .skill(spring)
                    .title("Secure the backend")
                    .description("Implement JWT authentication, protected routes, and per-user data access.")
                    .targetDate(LocalDate.now().plusWeeks(2))
                    .status(GoalStatus.IN_PROGRESS)
                    .build());

            goalRepository.save(LearningGoal.builder()
                    .user(user)
                    .skill(react)
                    .title("Ship a polished dashboard")
                    .description("Build forms, training screens, and a clean authenticated experience.")
                    .targetDate(LocalDate.now().plusWeeks(1))
                    .status(GoalStatus.PLANNED)
                    .build());

            Assessment springAssessment = assessmentRepository.save(Assessment.builder()
                    .user(user)
                    .skill(spring)
                    .title("Spring Boot Foundations Check")
                    .difficulty("Intermediate")
                    .questionCount(3)
                    .createdAt(Instant.now())
                    .build());

            questionRepository.save(AssessmentQuestion.builder()
                    .assessment(springAssessment)
                    .prompt("Which Spring Boot annotation marks the main application entry point?")
                    .optionA("@EnableWebMvc")
                    .optionB("@SpringBootApplication")
                    .optionC("@RestController")
                    .optionD("@ComponentScan")
                    .correctOption("B")
                    .build());

            questionRepository.save(AssessmentQuestion.builder()
                    .assessment(springAssessment)
                    .prompt("What is the default embedded server used by Spring Boot starter web?")
                    .optionA("Jetty")
                    .optionB("Undertow")
                    .optionC("Tomcat")
                    .optionD("Netty")
                    .correctOption("C")
                    .build());

            questionRepository.save(AssessmentQuestion.builder()
                    .assessment(springAssessment)
                    .prompt("Which layer should normally hold business logic in a Spring application?")
                    .optionA("Repository")
                    .optionB("Controller")
                    .optionC("Service")
                    .optionD("Entity")
                    .correctOption("C")
                    .build());

            Assessment reactAssessment = assessmentRepository.save(Assessment.builder()
                    .user(user)
                    .skill(react)
                    .title("React State and Effects")
                    .difficulty("Intermediate")
                    .questionCount(3)
                    .createdAt(Instant.now())
                    .build());

            questionRepository.save(AssessmentQuestion.builder()
                    .assessment(reactAssessment)
                    .prompt("Which hook is commonly used for state in function components?")
                    .optionA("useStore")
                    .optionB("useRef")
                    .optionC("useReducerOnly")
                    .optionD("useState")
                    .correctOption("D")
                    .build());

            questionRepository.save(AssessmentQuestion.builder()
                    .assessment(reactAssessment)
                    .prompt("What dependency array value runs an effect only once after mount in typical usage?")
                    .optionA("No array")
                    .optionB("[]")
                    .optionC("[true]")
                    .optionD("[props]")
                    .correctOption("B")
                    .build());

            questionRepository.save(AssessmentQuestion.builder()
                    .assessment(reactAssessment)
                    .prompt("Which pattern helps avoid passing props through many layers unnecessarily?")
                    .optionA("Context")
                    .optionB("Mutation")
                    .optionC("Console logging")
                    .optionD("Nested loops")
                    .correctOption("A")
                    .build());

            trainingResourceRepository.save(TrainingResource.builder()
                    .user(user)
                    .skill(spring)
                    .type(TrainingResourceType.PROJECT)
                    .title("JWT API Mini Project")
                    .description("Build login, protected endpoints, and a refreshable dashboard.")
                    .url("https://spring.io/guides")
                    .estimatedMinutes(90)
                    .build());

            trainingResourceRepository.save(TrainingResource.builder()
                    .user(user)
                    .skill(react)
                    .type(TrainingResourceType.PRACTICE)
                    .title("Dashboard UI Refactor")
                    .description("Convert a static dashboard into an authenticated data-driven experience.")
                    .url("https://react.dev/learn")
                    .estimatedMinutes(60)
                    .build());

            trainingResourceRepository.save(TrainingResource.builder()
                    .user(user)
                    .skill(sql)
                    .type(TrainingResourceType.ARTICLE)
                    .title("Query Optimization Basics")
                    .description("Review joins, indexes, and filtering patterns that matter in production.")
                    .url("https://www.postgresql.org/docs/")
                    .estimatedMinutes(45)
                    .build());

            Roadmap springRoadmap = Roadmap.builder()
                    .user(user)
                    .title("Spring Boot API Mastery")
                    .goal("Build production-ready backend services with security, persistence, and testing.")
                    .currentLevel("Intermediate")
                    .targetLevel("Advanced")
                    .introduction("This roadmap helps the demo user move from intermediate Spring Boot knowledge to advanced production-ready backend development.")
                    .projects(List.of(
                            "Build a secure task management API with JWT and role checks",
                            "Add integration tests and PostgreSQL-backed deployment configuration"
                    ))
                    .tips(List.of(
                            "Practice by building full request-to-database flows.",
                            "Focus on exception handling and test coverage in each sprint.",
                            "Review SQL generated by JPA and tune where needed."
                    ))
                    .progress(25)
                    .createdAt(Instant.now())
                    .build();

            RoadmapStep stepOne = RoadmapStep.builder()
                    .roadmap(springRoadmap)
                    .title("Stabilize application architecture")
                    .description("Separate controller, service, and repository concerns clearly and validate every input.")
                    .completed(true)
                    .stepOrder(1)
                    .build();
            stepOne.setResources(List.of(
                    RoadmapResource.builder()
                            .step(stepOne)
                            .type("Guide")
                            .title("Spring Boot reference")
                            .url("https://docs.spring.io/spring-boot/reference/")
                            .build()
            ));

            RoadmapStep stepTwo = RoadmapStep.builder()
                    .roadmap(springRoadmap)
                    .title("Secure every user-owned endpoint")
                    .description("Use JWT-based authentication and enforce ownership for skill, goal, and roadmap data.")
                    .completed(false)
                    .stepOrder(2)
                    .build();
            stepTwo.setResources(List.of(
                    RoadmapResource.builder()
                            .step(stepTwo)
                            .type("Practice")
                            .title("Spring Security architecture")
                            .url("https://docs.spring.io/spring-security/reference/")
                            .build()
            ));

            RoadmapStep stepThree = RoadmapStep.builder()
                    .roadmap(springRoadmap)
                    .title("Ship and test the project")
                    .description("Run the stack with PostgreSQL, validate APIs, and prepare the portfolio-ready submission.")
                    .completed(false)
                    .stepOrder(3)
                    .build();
            stepThree.setResources(List.of(
                    RoadmapResource.builder()
                            .step(stepThree)
                            .type("Project")
                            .title("PostgreSQL docs")
                            .url("https://www.postgresql.org/docs/")
                            .build()
            ));

            springRoadmap.setSteps(List.of(stepOne, stepTwo, stepThree));
            roadmapRepository.save(springRoadmap);
        };
    }
}
