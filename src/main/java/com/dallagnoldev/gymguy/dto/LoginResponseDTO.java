package com.dallagnoldev.gymguy.dto;

public record LoginResponseDTO(
        String token, Long expiresIn
) {
}
