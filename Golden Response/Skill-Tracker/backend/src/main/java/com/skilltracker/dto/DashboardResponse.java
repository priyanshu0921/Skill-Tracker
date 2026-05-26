package com.skilltracker.dto;

import java.util.List;

public record DashboardResponse(
        String userName,
        int totalSkills,
        int activeGoals,
        int assessmentsTaken,
        double averageConfidence,
        DailyGrowthResponse dailyGrowth,
        List<SkillResponse> focusSkills,
        List<GoalResponse> upcomingGoals,
        List<AssessmentAttemptResponse> recentAttempts,
        List<TrainingResourceResponse> recommendedTraining,
        List<String> insights
) {
}
