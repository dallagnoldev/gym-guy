package com.dallagnoldev.gymguy.dto;

import java.math.BigDecimal;

public record WorkoutExerciseResponseDTO(
        Long workoutId,
        Long exerciseId,
        BigDecimal weight,
        Integer reps,
        Integer sets,
        Integer position
) {
}
