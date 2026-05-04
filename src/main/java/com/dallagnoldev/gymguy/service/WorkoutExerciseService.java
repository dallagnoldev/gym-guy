package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.WorkoutExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutExerciseResponseDTO;
import com.dallagnoldev.gymguy.model.ExerciseEntity;
import com.dallagnoldev.gymguy.model.WorkoutEntity;
import com.dallagnoldev.gymguy.model.WorkoutExerciseEntity;
import com.dallagnoldev.gymguy.model.embeddable.WorkoutExerciseId;
import com.dallagnoldev.gymguy.repository.IExerciseRepository;
import com.dallagnoldev.gymguy.repository.IWorkoutExerciseRepository;
import com.dallagnoldev.gymguy.repository.IWorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {

    private final IWorkoutExerciseRepository workoutExerciseRepository;
    private final IWorkoutRepository workoutRepository;
    private final IExerciseRepository exerciseRepository;

    @Transactional
    public WorkoutExerciseResponseDTO addExerciseToWorkout(Long workoutId, Long exerciseId, WorkoutExerciseRequestDTO requestDTO) {
        WorkoutEntity workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));
        ExerciseEntity exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        WorkoutExerciseId id = new WorkoutExerciseId(workoutId, exerciseId);
        WorkoutExerciseEntity workoutExercise = WorkoutExerciseEntity.builder()
                .id(id)
                .workout(workout)
                .exercise(exercise)
                .weight(requestDTO.weight())
                .reps(requestDTO.reps())
                .sets(requestDTO.sets())
                .position(requestDTO.position())
                .build();

        return toResponse(workoutExerciseRepository.save(workoutExercise));
    }

    @Transactional(readOnly = true)
    public List<WorkoutExerciseResponseDTO> findExercisesByWorkoutId(Long workoutId) {
        if (!workoutRepository.existsById(workoutId)) {
            throw new EntityNotFoundException("Workout not found");
        }

        return workoutExerciseRepository.findAll().stream()
                .filter(we -> we.getId().getWorkoutId().equals(workoutId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeExerciseFromWorkout(Long workoutId, Long exerciseId) {
        WorkoutExerciseId id = new WorkoutExerciseId(workoutId, exerciseId);
        if (!workoutExerciseRepository.existsById(id)) {
            throw new EntityNotFoundException("WorkoutExercise not found");
        }
        workoutExerciseRepository.deleteById(id);
    }

    public WorkoutExerciseResponseDTO toResponse(WorkoutExerciseEntity entity) {
        return new WorkoutExerciseResponseDTO(
                entity.getId().getWorkoutId(),
                entity.getId().getExerciseId(),
                entity.getWeight(),
                entity.getReps(),
                entity.getSets(),
                entity.getPosition()
        );
    }
}
