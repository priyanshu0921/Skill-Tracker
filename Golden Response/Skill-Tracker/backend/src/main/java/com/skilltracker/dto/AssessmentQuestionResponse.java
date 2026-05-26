package com.skilltracker.dto;

public record AssessmentQuestionResponse(
        Long id,
        String prompt,
        String optionA,
        String optionB,
        String optionC,
        String optionD
) {
}
