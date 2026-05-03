package com.dallagnoldev.gymguy.dto;

import com.dallagnoldev.gymguy.model.enums.UserSexEnum;
import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank @Min(3) @Max(150) String firstName,
        @NotBlank @Min(2) @Max(150) String lastName,
        @NotBlank @Email String email,
        @NotBlank @Min(8) @Max(150) String password,
        @NotBlank @Min(8) @Max(30) String phoneNumber,
        @NotNull UserSexEnum sex,
        @NotNull @Positive Double height,
        @NotNull @Positive Double weight
) {
}
