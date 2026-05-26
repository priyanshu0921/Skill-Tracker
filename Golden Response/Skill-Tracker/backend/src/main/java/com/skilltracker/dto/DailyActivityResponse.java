package com.skilltracker.dto;

import java.time.LocalDate;

public record DailyActivityResponse(
        LocalDate date,
        boolean active
) {
}
