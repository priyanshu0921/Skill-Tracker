package com.skilltracker.service;

import com.skilltracker.dto.SkillRequest;
import com.skilltracker.dto.SkillResponse;
import com.skilltracker.model.AppUser;
import com.skilltracker.model.Assessment;
import com.skilltracker.model.AssessmentQuestion;
import com.skilltracker.model.ProficiencyLevel;
import com.skilltracker.model.Skill;
import com.skilltracker.model.TrainingResource;
import com.skilltracker.model.TrainingResourceType;
import com.skilltracker.repository.AssessmentQuestionRepository;
import com.skilltracker.repository.AssessmentRepository;
import com.skilltracker.repository.SkillRepository;
import com.skilltracker.repository.TrainingResourceRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final TrainingResourceRepository trainingResourceRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionRepository questionRepository;
    private final UserContextService userContextService;

    public SkillService(
            SkillRepository skillRepository,
            TrainingResourceRepository trainingResourceRepository,
            AssessmentRepository assessmentRepository,
            AssessmentQuestionRepository questionRepository,
            UserContextService userContextService
    ) {
        this.skillRepository = skillRepository;
        this.trainingResourceRepository = trainingResourceRepository;
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.userContextService = userContextService;
    }

    public List<SkillResponse> getMySkills() {
        AppUser user = userContextService.getCurrentUser();
        return skillRepository.findByUserOrderByPriorityRankAscNameAsc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SkillResponse createSkill(SkillRequest request) {
        AppUser user = userContextService.getCurrentUser();
        Skill skill = skillRepository.save(Skill.builder()
                .user(user)
                .name(request.name())
                .category(request.category())
                .proficiencyLevel(request.proficiencyLevel())
                .confidenceScore(request.confidenceScore())
                .targetScore(request.targetScore())
                .priorityRank(request.priorityRank())
                .lastPracticedOn(request.lastPracticedOn())
                .learningNote(request.learningNote())
                .build());

        createStarterTraining(user, skill);
        createStarterAssessment(user, skill);
        return toResponse(skill);
    }

    public SkillResponse updateSkill(Long skillId, SkillRequest request) {
        Skill skill = getOwnedSkill(skillId);
        skill.setName(request.name());
        skill.setCategory(request.category());
        skill.setProficiencyLevel(request.proficiencyLevel());
        skill.setConfidenceScore(request.confidenceScore());
        skill.setTargetScore(request.targetScore());
        skill.setPriorityRank(request.priorityRank());
        skill.setLastPracticedOn(request.lastPracticedOn());
        skill.setLearningNote(request.learningNote());
        return toResponse(skillRepository.save(skill));
    }

    public void deleteSkill(Long skillId) {
        AppUser user = userContextService.getCurrentUser();
        Skill skill = getOwnedSkill(skillId);
        skillRepository.deleteByIdAndUser(skill.getId(), user);
    }

    public Skill getOwnedSkill(Long skillId) {
        AppUser user = userContextService.getCurrentUser();
        return skillRepository.findByIdAndUser(skillId, user)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));
    }

    public void updateConfidenceFromAssessment(Skill skill, int score, int totalQuestions) {
        int percentage = (int) Math.round((score * 100.0) / totalQuestions);
        int updatedConfidence = Math.max(skill.getConfidenceScore(), percentage);
        skill.setConfidenceScore(updatedConfidence);
        skill.setLastPracticedOn(java.time.LocalDate.now());
        skill.setProficiencyLevel(mapProficiency(updatedConfidence));
        skillRepository.save(skill);
    }

    public SkillResponse toResponse(Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                skill.getProficiencyLevel(),
                skill.getConfidenceScore(),
                skill.getTargetScore(),
                skill.getPriorityRank(),
                skill.getLastPracticedOn(),
                skill.getLearningNote()
        );
    }

    private void createStarterTraining(AppUser user, Skill skill) {
        trainingResourceRepository.save(TrainingResource.builder()
                .user(user)
                .skill(skill)
                .type(TrainingResourceType.PRACTICE)
                .title(skill.getName() + " practice sprint")
                .description("Spend 45 minutes solving one practical task focused on " + skill.getName() + ".")
                .url("https://roadmap.sh")
                .estimatedMinutes(45)
                .build());
    }

    private void createStarterAssessment(AppUser user, Skill skill) {
        Assessment assessment = assessmentRepository.save(Assessment.builder()
                .user(user)
                .skill(skill)
                .title(skill.getName() + " Core Check")
                .difficulty("Starter")
                .questionCount(3)
                .createdAt(Instant.now())
                .build());

        questionRepository.save(AssessmentQuestion.builder()
                .assessment(assessment)
                .prompt("What is the strongest reason for learning " + skill.getName() + " in your current plan?")
                .optionA("It has no practical use")
                .optionB("It supports real project outcomes")
                .optionC("It replaces communication")
                .optionD("It removes the need for testing")
                .correctOption("B")
                .build());

        questionRepository.save(AssessmentQuestion.builder()
                .assessment(assessment)
                .prompt("Which approach best improves your " + skill.getName() + " ability?")
                .optionA("Consistent hands-on practice")
                .optionB("Avoiding projects")
                .optionC("Ignoring feedback")
                .optionD("Memorizing without building")
                .correctOption("A")
                .build());

        questionRepository.save(AssessmentQuestion.builder()
                .assessment(assessment)
                .prompt("What should you do after a weak score in " + skill.getName() + "?")
                .optionA("Stop tracking it")
                .optionB("Blame the tool")
                .optionC("Train, review, and retake")
                .optionD("Delete the goal")
                .correctOption("C")
                .build());
    }

    private ProficiencyLevel mapProficiency(int confidence) {
        if (confidence >= 90) {
            return ProficiencyLevel.EXPERT;
        }
        if (confidence >= 75) {
            return ProficiencyLevel.ADVANCED;
        }
        if (confidence >= 55) {
            return ProficiencyLevel.INTERMEDIATE;
        }
        return ProficiencyLevel.BEGINNER;
    }
}
