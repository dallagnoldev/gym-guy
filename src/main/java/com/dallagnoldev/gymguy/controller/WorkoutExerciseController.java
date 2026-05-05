package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.WorkoutExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutExerciseResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.service.WorkoutExerciseService;
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
@RequestMapping("api/v1/workouts/{workoutId}/exercises")
@RequiredArgsConstructor
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @PostMapping("/{exerciseId}")
    public ResponseEntity<WorkoutExerciseResponseDTO> addExercise(
            @PathVariable Long workoutId,
            @PathVariable Long exerciseId,
            @RequestBody @Valid WorkoutExerciseRequestDTO requestDTO) throws NotFoundException {


        WorkoutExerciseResponseDTO workoutExerciseResponseDTO = workoutExerciseService.addExerciseToWorkout(workoutId, exerciseId, requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(workoutId)
                .toUri();

        return ResponseEntity.created(location).body(workoutExerciseResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<WorkoutExerciseResponseDTO>> findByWorkout(
            @PathVariable Long workoutId,
            @PageableDefault(sort = "position", direction = Sort.Direction.ASC) Pageable pageable) throws NotFoundException {

        Page<WorkoutExerciseResponseDTO> workoutExerciseResponseDTO = workoutExerciseService.findExercisesByWorkoutId(workoutId, pageable);

        return ResponseEntity.ok(workoutExerciseResponseDTO);
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> removeExercise(@PathVariable Long workoutId, @PathVariable Long exerciseId) throws NotFoundException {
        workoutExerciseService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.noContent().build();
    }
}
