package com.dallagnoldev.gymguy.dto;

public record ExerciseResponseDTO(
        Long exerciseId,
        String name,
        String muscularGroup
) {
}
