package com.dallagnoldev.gymguy.dto.update;

import com.dallagnoldev.gymguy.model.enums.UserSexEnum;

public record UserUpdateRequestDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        UserSexEnum sex,
        Double height,
        Double weight
) {
}
