package com.dallagnoldev.gymguy.dto;

import java.util.List;

public record WorkoutResponseDTO(
        Long workoutId,
        String name,
        String description,
        List<WorkoutExerciseResponseDTO> exercises
) {
}
