package com.skilltracker.dto;

public record AuthResponse(
        String token,
        UserProfileResponse user
) {
}
