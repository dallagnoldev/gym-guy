package com.dallagnoldev.gymguy.dto;

import com.dallagnoldev.gymguy.model.enums.UserPlanTypeEnum;
import com.dallagnoldev.gymguy.model.enums.UserSexEnum;

import java.time.Instant;
import java.time.LocalDate;

public record UserResponseDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        UserSexEnum sex,
        Double height,
        Double weight,
        UserPlanTypeEnum planType,
        Instant createdAt,
        Instant updatedAt
) {
}
