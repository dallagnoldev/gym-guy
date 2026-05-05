package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.ExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.ExerciseResponseDTO;
import com.dallagnoldev.gymguy.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> create(@RequestBody @Valid ExerciseRequestDTO requestDTO) {
        ExerciseResponseDTO exerciseResponseDTO = exerciseService.createExercise(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(exerciseResponseDTO.exerciseId())
                .toUri();

        return ResponseEntity.created(location).body(exerciseResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.findExerciseById(id));
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> findAll() {
        return ResponseEntity.ok(exerciseService.findAllExercises());
    }

    @GetMapping("/search")
    public ResponseEntity<ExerciseResponseDTO> findByName(@RequestParam String name) {
        return ResponseEntity.ok(exerciseService.findExerciseByName(name));
    }

    @GetMapping("/muscular-group")
    public ResponseEntity<List<ExerciseResponseDTO>> findByMuscularGroup(@RequestParam String group) {
        return ResponseEntity.ok(exerciseService.findExercisesByMuscularGroup(group));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
