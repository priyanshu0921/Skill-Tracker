package com.skilltracker.dto;

import com.skilltracker.model.TrainingResourceType;

public record TrainingResourceResponse(
        Long id,
        Long skillId,
        String skillName,
        TrainingResourceType type,
        String title,
        String description,
        String url,
        Integer estimatedMinutes
) {
}
