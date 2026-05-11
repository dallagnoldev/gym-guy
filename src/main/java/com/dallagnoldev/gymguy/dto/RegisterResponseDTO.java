package com.dallagnoldev.gymguy.dto;

public record RegisterResponseDTO(
        Long userId,
        String firstName,
        String lastName,
        String email
) {
}
