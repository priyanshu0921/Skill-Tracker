package com.skilltracker.dto;

import jakarta.validation.constraints.NotBlank;

public record RoadmapRequest(
        @NotBlank String title,
        @NotBlank String goal,
        @NotBlank String currentLevel,
        @NotBlank String targetLevel
) {
}
