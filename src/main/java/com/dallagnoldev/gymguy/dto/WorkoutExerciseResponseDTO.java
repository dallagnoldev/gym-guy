package com.dallagnoldev.gymguy.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record WorkoutExerciseResponseDTO(
        Long workoutId,
        Long exerciseId,
        BigDecimal weight,
        Integer reps,
        Integer sets,
        Integer position,
        Instant createdAt,
        Instant updatedAt
) {
}
