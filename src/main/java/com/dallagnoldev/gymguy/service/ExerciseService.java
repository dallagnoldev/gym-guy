package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.ExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.ExerciseResponseDTO;
import com.dallagnoldev.gymguy.exception.ExerciseNameMustBeUniqueException;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.model.ExerciseEntity;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.repository.IExerciseRepository;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final IExerciseRepository exerciseRepository;
    private final IUserRepository userRepository;

    @Transactional
    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO, Long userId) throws ExerciseNameMustBeUniqueException {

        if (exerciseRepository.existsByNameIgnoreCase(exerciseRequestDTO.name())) {
            throw new ExerciseNameMustBeUniqueException("There is already an exercise with that name");
        }

        UserEntity exerciseOwner = null;

        if (userId != null) {
            exerciseOwner = userRepository.getReferenceById(userId);
        }

        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .name(exerciseRequestDTO.name())
                .muscularGroup(exerciseRequestDTO.muscularGroup())
                .user(exerciseOwner)
                .build();

        return toResponse(exerciseRepository.save(exerciseEntity));
    }

    @Transactional(readOnly = true)
    public ExerciseResponseDTO findExerciseById(Long exerciseId) throws NotFoundException {
        ExerciseEntity exerciseEntity = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));
        return toResponse(exerciseEntity);
    }

    @Transactional(readOnly = true)
    public Page<ExerciseResponseDTO> findAllExercises(Pageable pageable) {

        Page<ExerciseEntity> exerciseEntityPage = exerciseRepository.findAll(pageable);

        return exerciseEntityPage.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ExerciseResponseDTO findExerciseByName(String name) throws NotFoundException {
        ExerciseEntity exerciseEntity = exerciseRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));
        return toResponse(exerciseEntity);
    }

    @Transactional(readOnly = true)
    public Page<ExerciseResponseDTO> findExercisesByMuscularGroup(Pageable pageable, String muscularGroup) {

        Page<ExerciseEntity> exerciseEntityPage = exerciseRepository.findByMuscularGroupIgnoreCase(pageable, muscularGroup);

        return exerciseEntityPage.map(this::toResponse);
    }

    @Transactional
    public void deleteExercise(Long exerciseId) throws NotFoundException {
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new NotFoundException("Exercise not found");
        }
        exerciseRepository.deleteById(exerciseId);
    }

    public ExerciseResponseDTO toResponse(ExerciseEntity exerciseEntity) {
        return new ExerciseResponseDTO(
                exerciseEntity.getExerciseid(),
                exerciseEntity.getName(),
                exerciseEntity.getMuscularGroup(),
                exerciseEntity.getCreatedAt(),
                exerciseEntity.getUpdatedAt()
        );
    }
}
