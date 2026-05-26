package com.skilltracker.dto;

import java.util.List;

public record RoadmapStepResponse(
        Long id,
        String title,
        String description,
        boolean completed,
        int stepOrder,
        List<RoadmapResourceResponse> resources
) {
}
