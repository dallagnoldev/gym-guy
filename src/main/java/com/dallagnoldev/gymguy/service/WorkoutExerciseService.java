package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.WorkoutExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutExerciseResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.WorkoutExercisePositionInvalidException;
import com.dallagnoldev.gymguy.model.ExerciseEntity;
import com.dallagnoldev.gymguy.model.WorkoutEntity;
import com.dallagnoldev.gymguy.model.WorkoutExerciseEntity;
import com.dallagnoldev.gymguy.model.embeddable.WorkoutExerciseId;
import com.dallagnoldev.gymguy.repository.IExerciseRepository;
import com.dallagnoldev.gymguy.repository.IWorkoutExerciseRepository;
import com.dallagnoldev.gymguy.repository.IWorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public WorkoutExerciseResponseDTO addExerciseToWorkout(Long workoutId, Long exerciseId, WorkoutExerciseRequestDTO requestDTO) throws NotFoundException, WorkoutExercisePositionInvalidException {
        WorkoutEntity workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new NotFoundException("Workout not found"));
        ExerciseEntity exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));

        if (workoutExerciseRepository.existsByPosition(requestDTO.position())) {
            throw new WorkoutExercisePositionInvalidException("There is already exists an exercise registered in that position");
        }

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
    public Page<WorkoutExerciseResponseDTO> findExercisesByWorkoutId(Long workoutId, Pageable pageable) throws NotFoundException {
        if (!workoutRepository.existsById(workoutId)) {
            throw new NotFoundException("Workout not found");
        }

        Page<WorkoutExerciseEntity> workoutExerciseEntityPage = workoutExerciseRepository.findAllByWorkout_WorkoutId(pageable, workoutId);

        return workoutExerciseEntityPage.map(this::toResponse);

    }

    @Transactional
    public void removeExerciseFromWorkout(Long workoutId, Long exerciseId) throws NotFoundException {
        WorkoutExerciseId id = new WorkoutExerciseId(workoutId, exerciseId);
        if (!workoutExerciseRepository.existsById(id)) {
            throw new NotFoundException("Workout Exercise not found");
        }
        workoutExerciseRepository.deleteById(id);
    }

    public WorkoutExerciseResponseDTO toResponse(WorkoutExerciseEntity entity) {
        return new WorkoutExerciseResponseDTO(
                entity.getId().getExerciseId(),
                entity.getExercise().getName(),
                entity.getWeight(),
                entity.getReps(),
                entity.getSets(),
                entity.getPosition(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
