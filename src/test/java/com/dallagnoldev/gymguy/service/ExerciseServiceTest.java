package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.ExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.ExerciseResponseDTO;
import com.dallagnoldev.gymguy.exception.ExerciseNameMustBeUniqueException;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.model.ExerciseEntity;
import com.dallagnoldev.gymguy.repository.IExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @InjectMocks
    private ExerciseService exerciseService;

    @Mock
    private IExerciseRepository exerciseRepository;

    private ExerciseRequestDTO  exerciseRequestDTO;

    @BeforeEach
    public void setUp() {
        exerciseRequestDTO = new ExerciseRequestDTO(
                "Bench Press",
                "Chest"
        );
    }

    @Test
    @DisplayName("Should create exercise successfully")
    public void shouldCreateExerciseSuccessfully() throws ExerciseNameMustBeUniqueException {

        when(exerciseRepository.existsByNameIgnoreCase(exerciseRequestDTO.name())).thenReturn(false);
        when(exerciseRepository.save(any(ExerciseEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ExerciseResponseDTO response = exerciseService.createExercise(exerciseRequestDTO);

        assertNotNull(response);
        verify(exerciseRepository, times(1)).save(any(ExerciseEntity.class));
    }

    @Test
    @DisplayName("Should throw ExerciseNameMustBeUniqueException when exercise name already exists")
    public void shouldThrowExceptionWhenCreatingExerciseWithDuplicateName() {
        when(exerciseRepository.existsByNameIgnoreCase(exerciseRequestDTO.name())).thenReturn(true);

        assertThrows(ExerciseNameMustBeUniqueException.class, () -> exerciseService.createExercise(exerciseRequestDTO));

        verify(exerciseRepository, never()).save(any(ExerciseEntity.class));
    }

    @Test
    @DisplayName("Should find exercise by id successfully")
    public void shouldFindExerciseByIdSuccessfully() throws NotFoundException {
        Long id = 1L;

        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .exerciseid(id)
                .build();

        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exerciseEntity));

        ExerciseResponseDTO response = exerciseService.findExerciseById(id);

        assertEquals(id, response.exerciseId());

        verify(exerciseRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw NotFoundException when exercise id is not found")
    public void shouldThrowExceptionWhenExerciseIdNotFound() {
        Long id = 1L;
        when(exerciseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> exerciseService.findExerciseById(id));
    }

    @Test
    @DisplayName("Should find all exercises successfully")
    public void shouldFindAllExercisesSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);

        ExerciseEntity exerciseEntity = ExerciseEntity.builder().build();
        Page<ExerciseEntity> entityPage = new PageImpl<>(List.of(exerciseEntity));

        when(exerciseRepository.findAll(pageable)).thenReturn(entityPage);

        Page<ExerciseResponseDTO> response =  exerciseService.findAllExercises(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(exerciseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should find exercise by name successfully")
    public void shouldFindExerciseByNameSuccessfully() throws NotFoundException {
        String exerciseName = "Bench Press";

        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .name(exerciseName)
                .build();

        when(exerciseRepository.findByNameIgnoreCase(exerciseName)).thenReturn(Optional.of(exerciseEntity));

        ExerciseResponseDTO response = exerciseService.findExerciseByName(exerciseName);

        assertNotNull(response);
        assertEquals(exerciseName, response.name());

        verify(exerciseRepository, times(1)).findByNameIgnoreCase(exerciseName);
    }

    @Test
    @DisplayName("Should throw NotFoundException when exercise name is not found")
    public void shouldThrowExceptionWhenExerciseNameNotFound() {
        String name = "Non-existent";
        when(exerciseRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> exerciseService.findExerciseByName(name));
    }

    @Test
    @DisplayName("Should find exercises by muscular group successfully")
    public void shouldFindExerciseByMuscularGroupSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);

        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .name("Bench Press")
                .muscularGroup("Chest")
                .build();


        Page<ExerciseEntity> page = new PageImpl<>(List.of(exerciseEntity));

        when(exerciseRepository.findByMuscularGroupIgnoreCase(pageable, exerciseEntity.getMuscularGroup())).thenReturn(page);

        Page<ExerciseResponseDTO> response = exerciseService.findExercisesByMuscularGroup(pageable, exerciseRequestDTO.muscularGroup());

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        verify(exerciseRepository, times(1)).findByMuscularGroupIgnoreCase(pageable, exerciseRequestDTO.muscularGroup());
    }

    @Test
    @DisplayName("Should delete exercise successfully")
    public void shouldDeleteExerciseSuccessfully() throws NotFoundException {
        Long id = 1L;

        when(exerciseRepository.existsById(id)).thenReturn(true);

        exerciseService.deleteExercise(id);
        verify(exerciseRepository, times(1)).existsById(id);
        verify(exerciseRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existent exercise")
    public void shouldThrowExceptionWhenDeletingNonExistentExercise() {
        Long id = 1L;
        when(exerciseRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> exerciseService.deleteExercise(id));

        verify(exerciseRepository, never()).deleteById(anyLong());
    }
}
