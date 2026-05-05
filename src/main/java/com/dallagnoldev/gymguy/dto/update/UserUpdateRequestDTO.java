package com.dallagnoldev.gymguy.dto.update;

import com.dallagnoldev.gymguy.model.enums.UserSexEnum;

import java.time.LocalDate;

public record UserUpdateRequestDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        UserSexEnum sex,
        Double height,
        Double weight
) {
}
