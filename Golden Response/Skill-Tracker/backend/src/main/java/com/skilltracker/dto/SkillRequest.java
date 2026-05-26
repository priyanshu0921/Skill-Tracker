package com.skilltracker.dto;

import com.skilltracker.model.ProficiencyLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SkillRequest(
        @NotBlank String name,
        @NotBlank String category,
        @NotNull ProficiencyLevel proficiencyLevel,
        @NotNull @Min(1) @Max(100) Integer confidenceScore,
        @NotNull @Min(1) @Max(100) Integer targetScore,
        @NotNull @Min(1) @Max(10) Integer priorityRank,
        LocalDate lastPracticedOn,
        String learningNote
) {
}
