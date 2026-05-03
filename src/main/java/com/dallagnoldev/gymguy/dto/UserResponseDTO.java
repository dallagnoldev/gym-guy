package com.dallagnoldev.gymguy.dto;

import com.dallagnoldev.gymguy.model.enums.UserSexEnum;

public record UserResponseDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        UserSexEnum sex,
        Double height,
        Double weight
) {
}
