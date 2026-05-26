package com.skilltracker.dto;

import com.skilltracker.model.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GoalRequest(
        @NotNull Long skillId,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull LocalDate targetDate,
        @NotNull GoalStatus status
) {
}
