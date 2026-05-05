package com.dallagnoldev.gymguy.dto;

import java.time.Instant;

public record ExerciseResponseDTO(
        Long exerciseId,
        String name,
        String muscularGroup,
        Instant createdAt,
        Instant updatedAt
) {
}
