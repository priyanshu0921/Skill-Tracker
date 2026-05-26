package com.skilltracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssessmentAnswerRequest(
        @NotNull Long questionId,
        @NotBlank String selectedOption
) {
}
