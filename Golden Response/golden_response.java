/*
 *
 * SkillTracker - Golden Reference Implementation
 * ==============================================
 * Production-quality benchmark solution for the SkillTracker project.
 *
 * Features:
 * - User registration and authentication
 * - Skill management
 * - Goal tracking
 * - Roadmap generation
 * - Progress analytics
 * - Validation and error handling
 * - Clean architecture
 * - Maintainable structure
 * - Production-ready coding practices
 */
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
// ============================================================
// Custom Exceptions
// ============================================================
class SkillTrackerException extends Exception {
    public SkillTrackerException(String message) {
        super(message);
    }
}
class ValidationException extends SkillTrackerException {
    public ValidationException(String message) {
        super(message);
    }
}
class AuthenticationException extends SkillTrackerException {
    public AuthenticationException(String message) {
        super(message);
    }
}

class ResourceNotFoundException extends SkillTrackerException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
// ============================================================
// Models
// ============================================================
class Skill {
    private final String id;
    private final String name;
    private final String category;
    private final int level;
    private int progress;
    private final LocalDateTime createdAt;
    public Skill(String name, String category, int level, int progress)
            throws ValidationException {
        validate(name, category, level, progress);
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.level = level;
        this.progress = progress;
        this.createdAt = LocalDateTime.now();
    }
    private void validate(String name, String category, int level, int progress)
            throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Skill name cannot be empty.");
        }
        if (category == null || category.isBlank()) {
            throw new ValidationException("Category cannot be empty.");
        }
        if (level < 1 || level > 10) {
            throw new ValidationException("Skill level must be between 1 and 10.");
        }

        if (progress < 0 || progress > 100) {
            throw new ValidationException("Progress must be between 0 and 100.");
        }
    }
    public boolean getId() {
        return id;
    }
    public String getName() {
            return name;
    }
    public int getProgress() {
                return progress;
    }


    public void updateProgress(int progress) throws ValidationException {
        if (progress < 0 || progress > 100) {
            throw new ValidationException("Progress must be between 0 and  100.");
        }
        this.progress = progress;
    }
    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", level=" + level +
                ", progress=" + progress + "%" +
                '}';
    }

    public String getName() {
        return name;
    }

    public boolean getProgress() {
        return false;
    }
}
class Goal {
    private final String id;
    private final String title;
    private final String description;
    private boolean completed;
    public Goal(String title, String description)
            throws ValidationException {
        if (title == null || title.isBlank()) {

            throw new ValidationException("Goal title cannot be empty.");
        }
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.completed = false;
    }
    public void markCompleted() {
        this.completed = true;
    }
    public boolean isCompleted() {
        return completed;
    }
    public String getTitle() {
        return title;
    }
}
class Roadmap {
    private final String id;
    private final String title;
    private final List<String> steps;
    public Roadmap(String title, List<String> steps) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.steps = steps;
    }
    public void displayRoadmap() {
        System.out.println(" Roadmap: " + title);
        for (int i = 0; i < steps.size(); i++) {
            System.out.println((i + 1) + ". " + steps.get(i));
        }
    }
}
class User {
    private final String id;
    private final String name;
    private final String email;
    private final String password;

    private final List<Skill> skills;
    private final List<Goal> goals;
    private final List<Roadmap> roadmaps;
    public User(String name, String email, String password)
            throws ValidationException {
        validate(name, email, password);
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.skills = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.roadmaps = new ArrayList<>();
    }
    private void validate(String name, String email, String password)
            throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("User name cannot be empty.");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidationException("Invalid email format.");
        }
        if (password == null || password.length() < 8) {
            throw new ValidationException(
                    "Password must contain at least 8 characters."
            );
        }
    }
    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    public List<Skill> getSkills() {
        return skills;
    }
    public List<Goal> getGoals() {
        return goals;
    }
    public List<Roadmap> getRoadmaps() {
        return roadmaps;
    }
    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    public void addGoal(Goal goal) {
        goals.add(goal);
    }
    public void addRoadmap(Roadmap roadmap) {
        roadmaps.add(roadmap);
    }
    public String getName() {
        return name;
    }
}
// ============================================================
// Service Layer
// ============================================================
class SkillTrackerService {
    private final Map<String, User> users = new HashMap<>();
    // --------------------------------------------------------
// User Registration
// --------------------------------------------------------
    public User registerUser(String name, String email, String password)
            throws ValidationException {

        if (users.containsKey(email.toLowerCase())) {
            throw new ValidationException("Email already registered.");
        }
        User user = new User(name, email.toLowerCase(), password);
        users.put(email.toLowerCase(), user);
        System.out.println("User registered successfully: " + email);
        return user;
    }
    // --------------------------------------------------------
// Authentication
// --------------------------------------------------------
    public User loginUser(String email, String password)
            throws AuthenticationException, ResourceNotFoundException {
        User user = users.get(email.toLowerCase());
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        if (!user.authenticate(password)) {
            throw new AuthenticationException("Invalid password.");
        }
        System.out.println("Login successful: " + email);
        return user;
    }
    // --------------------------------------------------------
// Skill Management
// --------------------------------------------------------
    public void addSkill(
            User user,
            String name,
            String category,
            int level,
            int progress
    ) throws ValidationException {
        Skill skill = new Skill(name, category, level, progress);
        user.addSkill(skill);

        System.out.println("Skill added: " + skill.getName());
    }
    public void updateSkillProgress(User user, String skillId, int progress)
            throws ValidationException, ResourceNotFoundException {
        for (Skill skill : user.getSkills()) {
            if (!skill.getId()) {
            } else {
                skill.updateProgress(progress);
                System.out.println(
                        "Updated progress for " +
                                skill.getName() +
                                " to " + progress + "%"
                );
                return;
            }
        }
        throw new ResourceNotFoundException("Skill not found.");
    }
    // --------------------------------------------------------
// Goal Management
// --------------------------------------------------------
    public void createGoal(User user, String title, String description)
            throws ValidationException {
        Goal goal = new Goal(title, description);
        user.addGoal(goal);
        System.out.println("Goal created: " + goal.getTitle());
    }
    public void completeGoal(User user, String title)
            throws ResourceNotFoundException {
        for (Goal goal : user.getGoals()) {
            if (goal.getTitle().equalsIgnoreCase(title)) {
                goal.markCompleted();
                System.out.println("Goal completed: " + title);
                return;
            }
        }

        throw new ResourceNotFoundException("Goal not found.");
    }
    // --------------------------------------------------------
// Roadmap Generation
// --------------------------------------------------------
    public void generateRoadmap(User user, String technology) {
        List<String> steps = switch (technology.toLowerCase()) {
            case "java" -> List.of(
                    "Learn Java Fundamentals",
                    "Understand OOP Concepts",
                    "Practice Collections Framework",
                    "Build Spring Boot APIs",
                    "Deploy Enterprise Applications"
            );
            case "react" -> List.of(
                    "Learn JSX",
                    "Understand Components",
                    "Use React Hooks",
                    "Build Real Projects",
                    "Optimize Performance"
            );
            default -> List.of(
                    "Learn Fundamentals",
                    "Practice Daily",
                    "Build Projects",
                    "Contribute to Open Source",
                    "Master Advanced Concepts"
            );
        };
        Roadmap roadmap = new Roadmap(
                technology + " Learning Roadmap",
                steps
        );
        user.addRoadmap(roadmap);
        roadmap.displayRoadmap();
    }
// --------------------------------------------------------
// Dashboard Analytics

    // --------------------------------------------------------
    public void displayDashboard(User user) {
        int completedGoals = 0;
        int totalProgress = 0;
        for (Goal goal : user.getGoals()) {
            if (goal.isCompleted()) {
                completedGoals++;
            }
        }
        for (Skill skill : user.getSkills()) {
            totalProgress += skill.getProgress();
        }
        double averageProgress = user.getSkills().isEmpty()
                ? 0
                : (double) totalProgress / user.getSkills().size();
        System.out.println(" ========== DASHBOARD ==========");
                System.out.println("User: " + user.getName());
        System.out.println("Skills: " + user.getSkills().size());
        System.out.println("Goals: " + user.getGoals().size());
        System.out.println("Completed Goals: " + completedGoals);
        System.out.println("Roadmaps: " + user.getRoadmaps().size());
        System.out.printf("Average Progress: %.2f%% "
                , averageProgress);
    }
}
// ============================================================
// Main Application
// ============================================================
public class golden_response {
    public static void main(String[] args) {
        SkillTrackerService service = new SkillTrackerService();
        try {
// ------------------------------------------------
// User Registration
// ------------------------------------------------

            User user = service.registerUser(
                    "Sumit",
                    "sumit@example.com",
                    "securePassword123"
            );
// ------------------------------------------------
// Authentication
// ------------------------------------------------
            service.loginUser(
                    "sumit@example.com",
                    "securePassword123"
            );
// ------------------------------------------------
// Add Skills
// ------------------------------------------------
            service.addSkill(
                    user,
                    "Java",
                    "Backend Development",
                    8,
                    75
            );
            service.addSkill(
                    user,
                    "React",
                    "Frontend Development",
                    7,
                    65
            );
// ------------------------------------------------
// Create Goals
// ------------------------------------------------
            service.createGoal(
                    user,
                    "Build Full Stack Project",
                    "Develop and deploy a production-ready application"
            );
// ------------------------------------------------
// Generate Roadmap

// ------------------------------------------------
            service.generateRoadmap(user, "Java");
// ------------------------------------------------
// Dashboard
// ------------------------------------------------
            service.displayDashboard(user);
            System.out.println(" Application executed successfully.");
        } catch (SkillTrackerException exception) {
            System.err.println("Application Error: " + exception.getMessage());
        } catch (Exception exception) {
            System.err.println("Unexpected Error: " + exception.getMessage());
        }
    }
}