package com.dallagnoldev.gymguy.dto;

import com.dallagnoldev.gymguy.model.enums.UserSexEnum;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequestDTO(
        @NotBlank @Size(min = 3, max = 150) String firstName,
        @NotBlank @Size(min = 2, max = 150) String lastName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 150) String password,
        @NotBlank String phoneNumber,
        @NotNull LocalDate birthDate,
        @NotNull UserSexEnum sex,
        @NotNull @Positive Double height,
        @NotNull @Positive Double weight
) {
}
