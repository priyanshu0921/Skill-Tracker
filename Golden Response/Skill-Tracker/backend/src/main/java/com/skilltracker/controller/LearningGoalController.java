package com.skilltracker.controller;

import com.skilltracker.dto.GoalRequest;
import com.skilltracker.dto.GoalResponse;
import com.skilltracker.service.LearningGoalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/goals")
public class LearningGoalController {

    private final LearningGoalService goalService;

    public LearningGoalController(LearningGoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public List<GoalResponse> getGoals() {
        return goalService.getMyGoals();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse createGoal(@Valid @RequestBody GoalRequest request) {
        return goalService.createGoal(request);
    }

    @PutMapping("/{goalId}")
    public GoalResponse updateGoal(@PathVariable Long goalId, @Valid @RequestBody GoalRequest request) {
        return goalService.updateGoal(goalId, request);
    }

    @DeleteMapping("/{goalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
    }
}
