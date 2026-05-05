package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.WorkoutRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/users/{userId}/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> create(
            @PathVariable Long userId,
            @RequestBody @Valid WorkoutRequestDTO requestDTO) throws NotFoundException {

        WorkoutResponseDTO responseDTO = workoutService.createWorkout(userId, requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.workoutId())
                .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<WorkoutResponseDTO>> findAllByUser(
            @PathVariable Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) throws NotFoundException {

        Page<WorkoutResponseDTO> workoutResponseDTO = workoutService.findAllWorkoutsByUserId(pageable, userId);

        return ResponseEntity.ok(workoutResponseDTO);
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseDTO> findById(@PathVariable Long workoutId) throws NotFoundException {
        return ResponseEntity.ok(workoutService.findWorkoutById(workoutId));
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> delete(@PathVariable Long workoutId) throws NotFoundException {
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.noContent().build();
    }
}
