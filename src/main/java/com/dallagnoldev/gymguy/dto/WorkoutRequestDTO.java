package com.dallagnoldev.gymguy.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record WorkoutRequestDTO(
        @NotBlank @Min(3) @Max(150) String name,
        @NotBlank @Min(4) @Max(255) String description
) {
}
