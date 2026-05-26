package com.skilltracker.dto;

import java.time.Instant;
import java.util.List;

public record AssessmentResponse(
        Long id,
        Long skillId,
        String skillName,
        String title,
        String difficulty,
        Integer questionCount,
        Instant createdAt,
        List<AssessmentQuestionResponse> questions
) {
}
