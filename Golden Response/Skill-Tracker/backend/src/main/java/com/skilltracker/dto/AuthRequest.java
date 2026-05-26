package com.skilltracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public record AuthRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
