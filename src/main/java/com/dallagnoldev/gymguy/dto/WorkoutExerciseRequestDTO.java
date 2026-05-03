package com.dallagnoldev.gymguy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WorkoutExerciseRequestDTO(
    @NotNull @Positive BigDecimal weight,
    @NotNull @Positive Integer reps,
    @NotNull @Positive Integer sets,
    @NotNull @Positive Integer position
) {
}
