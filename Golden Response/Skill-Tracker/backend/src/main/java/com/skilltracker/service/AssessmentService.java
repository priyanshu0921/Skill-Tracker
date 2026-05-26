package com.skilltracker.service;

import com.skilltracker.dto.AssessmentAnswerRequest;
import com.skilltracker.dto.AssessmentAttemptResponse;
import com.skilltracker.dto.AssessmentQuestionResponse;
import com.skilltracker.dto.AssessmentResponse;
import com.skilltracker.dto.AssessmentSubmissionRequest;
import com.skilltracker.model.AppUser;
import com.skilltracker.model.Assessment;
import com.skilltracker.model.AssessmentAttempt;
import com.skilltracker.model.AssessmentQuestion;
import com.skilltracker.model.Skill;
import com.skilltracker.repository.AssessmentAttemptRepository;
import com.skilltracker.repository.AssessmentQuestionRepository;
import com.skilltracker.repository.AssessmentRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final UserContextService userContextService;
    private final SkillService skillService;

    public AssessmentService(
            AssessmentRepository assessmentRepository,
            AssessmentQuestionRepository questionRepository,
            AssessmentAttemptRepository attemptRepository,
            UserContextService userContextService,
            SkillService skillService
    ) {
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.userContextService = userContextService;
        this.skillService = skillService;
    }

    public List<AssessmentResponse> getMyAssessments() {
        AppUser user = userContextService.getCurrentUser();
        return assessmentRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AssessmentAttemptResponse submit(Long assessmentId, AssessmentSubmissionRequest request) {
        AppUser user = userContextService.getCurrentUser();
        Assessment assessment = assessmentRepository.findByIdAndUser(assessmentId, user)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));

        List<AssessmentQuestion> questions = questionRepository.findByAssessmentOrderByIdAsc(assessment);
        Map<Long, AssessmentQuestion> questionMap = questions.stream()
                .collect(java.util.stream.Collectors.toMap(AssessmentQuestion::getId, Function.identity()));

        int score = 0;
        for (AssessmentAnswerRequest answer : request.answers()) {
            AssessmentQuestion question = questionMap.get(answer.questionId());
            if (question != null && question.getCorrectOption().equalsIgnoreCase(answer.selectedOption())) {
                score++;
            }
        }

        AssessmentAttempt attempt = attemptRepository.save(AssessmentAttempt.builder()
                .assessment(assessment)
                .user(user)
                .score(score)
                .totalQuestions(questions.size())
                .attemptedAt(Instant.now())
                .feedback(buildFeedback(score, questions.size(), assessment.getSkill().getName()))
                .build());

        Skill skill = assessment.getSkill();
        skillService.updateConfidenceFromAssessment(skill, score, questions.size());

        return toAttemptResponse(attempt);
    }

    public List<AssessmentAttemptResponse> getRecentAttempts() {
        AppUser user = userContextService.getCurrentUser();
        return attemptRepository.findTop5ByUserOrderByAttemptedAtDesc(user)
                .stream()
                .map(this::toAttemptResponse)
                .toList();
    }

    private AssessmentResponse toResponse(Assessment assessment) {
        List<AssessmentQuestionResponse> questions = questionRepository.findByAssessmentOrderByIdAsc(assessment)
                .stream()
                .map(question -> new AssessmentQuestionResponse(
                        question.getId(),
                        question.getPrompt(),
                        question.getOptionA(),
                        question.getOptionB(),
                        question.getOptionC(),
                        question.getOptionD()
                ))
                .toList();

        return new AssessmentResponse(
                assessment.getId(),
                assessment.getSkill().getId(),
                assessment.getSkill().getName(),
                assessment.getTitle(),
                assessment.getDifficulty(),
                assessment.getQuestionCount(),
                assessment.getCreatedAt(),
                questions
        );
    }

    private AssessmentAttemptResponse toAttemptResponse(AssessmentAttempt attempt) {
        return new AssessmentAttemptResponse(
                attempt.getId(),
                attempt.getAssessment().getId(),
                attempt.getAssessment().getTitle(),
                attempt.getAssessment().getSkill().getName(),
                attempt.getScore(),
                attempt.getTotalQuestions(),
                attempt.getAttemptedAt(),
                attempt.getFeedback()
        );
    }

    private String buildFeedback(int score, int total, String skillName) {
        int percentage = (int) Math.round(score * 100.0 / total);
        if (percentage >= 85) {
            return "Excellent work in " + skillName + ". You are ready for more advanced project-based challenges.";
        }
        if (percentage >= 60) {
            return "Solid progress in " + skillName + ". Review the weak spots and retake the assessment after practice.";
        }
        return "Your " + skillName + " fundamentals need reinforcement. Focus on training resources before the next attempt.";
    }
}
