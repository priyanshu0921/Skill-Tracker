package com.skilltracker.dto;

import java.time.Instant;

public record AssessmentAttemptResponse(
        Long id,
        Long assessmentId,
        String assessmentTitle,
        String skillName,
        Integer score,
        Integer totalQuestions,
        Instant attemptedAt,
        String feedback
) {
}
