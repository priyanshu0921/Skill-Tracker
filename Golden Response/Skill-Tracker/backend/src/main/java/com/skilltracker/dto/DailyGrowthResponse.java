package com.skilltracker.dto;

import java.util.List;

public record DailyGrowthResponse(
        int currentStreak,
        int longestStreak,
        boolean practicedToday,
        int activeDaysThisWeek,
        int weeklyTarget,
        List<DailyActivityResponse> recentActivity,
        String momentumLabel,
        String nextMilestone
) {
}
