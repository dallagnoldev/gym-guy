package com.dallagnoldev.gymguy.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record WorkoutExerciseResponseDTO(
        Long exerciseId,
        String exerciseName,
        BigDecimal weight,
        Integer reps,
        Integer sets,
        Integer position,
        Instant createdAt,
        Instant updatedAt
) {
}
