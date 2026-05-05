package com.dallagnoldev.gymguy.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {
    private String message;
    private Integer status;
}
