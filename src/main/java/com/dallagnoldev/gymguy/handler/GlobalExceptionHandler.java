package com.dallagnoldev.gymguy.handler;

import com.dallagnoldev.gymguy.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handlePasswordInvalidException(PasswordInvalidException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(WorkoutNameMustBeUniqueException.class)
    public ResponseEntity<ErrorResponseDTO> handleWorkoutNameMustBeUniqueException(WorkoutNameMustBeUniqueException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ExerciseNameMustBeUniqueException.class)
    public ResponseEntity<ErrorResponseDTO> handleExerciseNameMustBeUniqueException(ExerciseNameMustBeUniqueException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(WorkoutQuantityLimitException.class)
    public ResponseEntity<ErrorResponseDTO> handleWorkoutQuantityLimitException(WorkoutQuantityLimitException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(WorkoutExercisePositionInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleWorkoutExercisePositionInvalidException(WorkoutExercisePositionInvalidException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
