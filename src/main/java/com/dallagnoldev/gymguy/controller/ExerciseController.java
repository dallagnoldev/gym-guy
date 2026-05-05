package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.ExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.ExerciseResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.service.ExerciseService;
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
    public ResponseEntity<ExerciseResponseDTO> findById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(exerciseService.findExerciseById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ExerciseResponseDTO>> findAll(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ExerciseResponseDTO> exerciseResponseDTO = exerciseService.findAllExercises(pageable);

        return ResponseEntity.ok(exerciseResponseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<ExerciseResponseDTO> findByName(@RequestParam String name) throws NotFoundException {
        return ResponseEntity.ok(exerciseService.findExerciseByName(name));
    }

    @GetMapping("/muscular-group")
    public ResponseEntity<Page<ExerciseResponseDTO>> findByMuscularGroup(
            @RequestParam String group,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ExerciseResponseDTO> exerciseResponseDTO = exerciseService.findExercisesByMuscularGroup(pageable, group);
        return ResponseEntity.ok(exerciseResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
