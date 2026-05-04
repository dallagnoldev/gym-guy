package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.ExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.ExerciseResponseDTO;
import com.dallagnoldev.gymguy.model.ExerciseEntity;
import com.dallagnoldev.gymguy.repository.IExerciseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final IExerciseRepository exerciseRepository;

    @Transactional
    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO) {
        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .name(exerciseRequestDTO.name())
                .muscularGroup(exerciseRequestDTO.muscularGroup())
                .build();

        return toResponse(exerciseRepository.save(exerciseEntity));
    }

    @Transactional(readOnly = true)
    public ExerciseResponseDTO findExerciseById(Long exerciseId) {
        ExerciseEntity exerciseEntity = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
        return toResponse(exerciseEntity);
    }

    @Transactional(readOnly = true)
    public List<ExerciseResponseDTO> findAllExercises() {
        return exerciseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExerciseResponseDTO findExerciseByName(String name) {
        ExerciseEntity exerciseEntity = exerciseRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
        return toResponse(exerciseEntity);
    }

    @Transactional(readOnly = true)
    public List<ExerciseResponseDTO> findExercisesByMuscularGroup(String muscularGroup) {
        return exerciseRepository.findByMuscularGroup(muscularGroup).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteExercise(Long exerciseId) {
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new EntityNotFoundException("Exercise not found");
        }
        exerciseRepository.deleteById(exerciseId);
    }

    public ExerciseResponseDTO toResponse(ExerciseEntity exerciseEntity) {
        return new ExerciseResponseDTO(
                exerciseEntity.getExerciseid(),
                exerciseEntity.getName(),
                exerciseEntity.getMuscularGroup()
        );
    }
}
