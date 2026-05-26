package com.skilltracker.dto;

public record UserProfileResponse(
        Long id,
        String fullName,
        String email
) {
}
