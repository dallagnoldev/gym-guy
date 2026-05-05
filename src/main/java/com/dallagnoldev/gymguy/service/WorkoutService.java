package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.WorkoutRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutResponseDTO;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.model.WorkoutEntity;
import com.dallagnoldev.gymguy.repository.IUserRepository;
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
public class WorkoutService {

    private final IWorkoutRepository workoutRepository;
    private final IUserRepository userRepository;
    private final WorkoutExerciseService workoutExerciseService;

    @Transactional
    public WorkoutResponseDTO createWorkout(Long userId, WorkoutRequestDTO workoutRequestDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        WorkoutEntity workoutEntity = WorkoutEntity.builder()
                .name(workoutRequestDTO.name())
                .description(workoutRequestDTO.description())
                .userId(user)
                .build();

        return toResponse(workoutRepository.save(workoutEntity));
    }

    @Transactional(readOnly = true)
    public WorkoutResponseDTO findWorkoutById(Long workoutId) {
        WorkoutEntity workoutEntity = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));
        return toResponse(workoutEntity);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutResponseDTO> findAllWorkoutsByUserId(Pageable pageable, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        Page<WorkoutEntity> workoutEntityPage = workoutRepository.findAllWorkoutsByUserId_UserId(userId, pageable);

        return workoutEntityPage.map(this::toResponse);
    }

    @Transactional
    public void deleteWorkout(Long workoutId) {
        if (!workoutRepository.existsById(workoutId)) {
            throw new EntityNotFoundException("Workout not found");
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
