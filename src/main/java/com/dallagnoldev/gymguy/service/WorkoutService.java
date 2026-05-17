package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.WorkoutRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.WorkoutNameMustBeUniqueException;
import com.dallagnoldev.gymguy.exception.QuantityLimitException;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.model.WorkoutEntity;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import com.dallagnoldev.gymguy.repository.IWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final IWorkoutRepository workoutRepository;
    private final IUserRepository userRepository;
    private final WorkoutExerciseService workoutExerciseService;


    // future: create a strategy for different type of users
    private static final int WORKOUT_QUANTITY_LIMIT = 10;

    @Transactional
    public WorkoutResponseDTO createWorkout(Long userId, WorkoutRequestDTO workoutRequestDTO) throws NotFoundException, WorkoutNameMustBeUniqueException, QuantityLimitException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (workoutRepository.existsByNameIgnoreCase(workoutRequestDTO.name())) {
            throw new WorkoutNameMustBeUniqueException("There is already exists a workout with that name");
        }

        long totalWorkouts = workoutRepository.countAllWorkoutsByUserId_UserId(userId);

        if (totalWorkouts >= WORKOUT_QUANTITY_LIMIT) {
            throw new QuantityLimitException("You reached your workout creation limit");
        }

        WorkoutEntity workoutEntity = WorkoutEntity.builder()
                .name(workoutRequestDTO.name())
                .description(workoutRequestDTO.description())
                .userId(user)
                .build();

        return toResponse(workoutRepository.save(workoutEntity));
    }

    @Transactional(readOnly = true)
    public WorkoutResponseDTO findWorkoutById(Long workoutId) throws NotFoundException {
        WorkoutEntity workoutEntity = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new NotFoundException("Workout not found"));
        return toResponse(workoutEntity);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutResponseDTO> findAllWorkoutsByUserId(Pageable pageable, Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }

        Page<WorkoutEntity> workoutEntityPage = workoutRepository.findAllWorkoutsByUserId_UserId(userId, pageable);

        return workoutEntityPage.map(this::toResponse);
    }

    @Transactional
    public void deleteWorkout(Long workoutId) throws NotFoundException {
        if (!workoutRepository.existsById(workoutId)) {
            throw new NotFoundException("Workout not found");
        }
        workoutRepository.deleteById(workoutId);
    }

    public WorkoutResponseDTO toResponse(WorkoutEntity workoutEntity) {
        return new WorkoutResponseDTO(
                workoutEntity.getWorkoutId(),
                workoutEntity.getName(),
                workoutEntity.getDescription(),
                workoutEntity.getExercises().stream()
                        .map(workoutExerciseService::toResponse)
                        .collect(Collectors.toList()),
                workoutEntity.getCreatedAt(),
                workoutEntity.getUpdatedAt()
        );
    }
}
