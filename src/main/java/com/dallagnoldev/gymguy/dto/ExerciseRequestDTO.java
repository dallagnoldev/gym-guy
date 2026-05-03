package com.dallagnoldev.gymguy.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ExerciseRequestDTO(
        @NotBlank @Min(4) @Max(150) String name,
        @NotBlank @Max(40) String muscularGroup
) {
}
