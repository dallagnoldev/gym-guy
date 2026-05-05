package com.dallagnoldev.gymguy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExerciseRequestDTO(
        @NotBlank @Size(min = 4, max = 150) String name,
        @NotBlank @Size(max = 40) String muscularGroup
) {
}
