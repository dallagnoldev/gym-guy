package com.dallagnoldev.gymguy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkoutRequestDTO(
        @NotBlank @Size(min = 3, max = 150) String name,
        @NotBlank @Size(min = 4, max = 255) String description
) {
}
