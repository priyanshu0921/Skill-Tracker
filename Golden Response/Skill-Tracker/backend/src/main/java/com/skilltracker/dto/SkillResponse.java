package com.skilltracker.dto;

import com.skilltracker.model.ProficiencyLevel;
import java.time.LocalDate;

public record SkillResponse(
        Long id,
        String name,
        String category,
        ProficiencyLevel proficiencyLevel,
        Integer confidenceScore,
        Integer targetScore,
        Integer priorityRank,
        LocalDate lastPracticedOn,
        String learningNote
) {
}
