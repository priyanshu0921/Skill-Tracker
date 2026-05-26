package com.skilltracker.service;

import com.skilltracker.dto.GoalRequest;
import com.skilltracker.dto.GoalResponse;
import com.skilltracker.model.AppUser;
import com.skilltracker.model.LearningGoal;
import com.skilltracker.model.Skill;
import com.skilltracker.repository.LearningGoalRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LearningGoalService {

    private final LearningGoalRepository goalRepository;
    private final SkillService skillService;
    private final UserContextService userContextService;

    public LearningGoalService(
            LearningGoalRepository goalRepository,
            SkillService skillService,
            UserContextService userContextService
    ) {
        this.goalRepository = goalRepository;
        this.skillService = skillService;
        this.userContextService = userContextService;
    }

    public List<GoalResponse> getMyGoals() {
        AppUser user = userContextService.getCurrentUser();
        return goalRepository.findByUserOrderByTargetDateAsc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GoalResponse createGoal(GoalRequest request) {
        AppUser user = userContextService.getCurrentUser();
        Skill skill = skillService.getOwnedSkill(request.skillId());
        LearningGoal goal = goalRepository.save(LearningGoal.builder()
                .user(user)
                .skill(skill)
                .title(request.title())
                .description(request.description())
                .targetDate(request.targetDate())
                .status(request.status())
                .build());

        return toResponse(goal);
    }

    public GoalResponse updateGoal(Long goalId, GoalRequest request) {
        AppUser user = userContextService.getCurrentUser();
        LearningGoal goal = goalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        Skill skill = skillService.getOwnedSkill(request.skillId());

        goal.setSkill(skill);
        goal.setTitle(request.title());
        goal.setDescription(request.description());
        goal.setTargetDate(request.targetDate());
        goal.setStatus(request.status());
        return toResponse(goalRepository.save(goal));
    }

    public void deleteGoal(Long goalId) {
        AppUser user = userContextService.getCurrentUser();
        LearningGoal goal = goalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        goalRepository.deleteByIdAndUser(goal.getId(), user);
    }

    public GoalResponse toResponse(LearningGoal goal) {
        return new GoalResponse(
                goal.getId(),
                goal.getSkill().getId(),
                goal.getSkill().getName(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getTargetDate(),
                goal.getStatus()
        );
    }
}
