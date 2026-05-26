package com.skilltracker.dto;

import com.skilltracker.model.GoalStatus;
import java.time.LocalDate;

public record GoalResponse(
        Long id,
        Long skillId,
        String skillName,
        String title,
        String description,
        LocalDate targetDate,
        GoalStatus status
) {
}
