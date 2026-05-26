package com.skilltracker.dto;

import java.util.List;

public record RoadmapResponse(
        Long id,
        String title,
        String goal,
        String currentLevel,
        String targetLevel,
        String introduction,
        int progress,
        List<RoadmapStepResponse> steps,
        List<String> projects,
        List<String> tips
) {
}
