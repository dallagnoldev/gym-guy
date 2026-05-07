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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseServiceTest {

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    @Mock
    private IWorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private IWorkoutRepository workoutRepository;

    @Mock
    private IExerciseRepository exerciseRepository;

    private WorkoutExerciseRequestDTO requestDTO;
    private WorkoutEntity workoutEntity;
    private ExerciseEntity exerciseEntity;
    private WorkoutExerciseEntity workoutExerciseEntity;

    @BeforeEach
    public void setUp() {
        requestDTO = new WorkoutExerciseRequestDTO(
                BigDecimal.valueOf(100.0),
                10,
                3,
                1
        );

        workoutEntity = WorkoutEntity.builder()
                .workoutId(1L)
                .name("Workout A")
                .build();

        exerciseEntity = ExerciseEntity.builder()
                .exerciseid(1L)
                .name("Bench Press")
                .build();

        workoutExerciseEntity = WorkoutExerciseEntity.builder()
                .id(new WorkoutExerciseId(1L, 1L))
                .workout(workoutEntity)
                .exercise(exerciseEntity)
                .weight(BigDecimal.valueOf(100.0))
                .reps(10)
                .sets(3)
                .position(1)
                .build();
    }

    @Test
    @DisplayName("Should add exercise to workout successfully")
    public void shouldAddExerciseToWorkoutSuccessfully() throws NotFoundException, WorkoutExercisePositionInvalidException {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exerciseEntity));
        when(workoutExerciseRepository.existsByPosition(1)).thenReturn(false);
        when(workoutExerciseRepository.save(any(WorkoutExerciseEntity.class))).thenReturn(workoutExerciseEntity);

        WorkoutExerciseResponseDTO response = workoutExerciseService.addExerciseToWorkout(1L, 1L, requestDTO);

        assertNotNull(response);
        assertEquals(1, response.position());
        verify(workoutExerciseRepository, times(1)).save(any(WorkoutExerciseEntity.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when workout not found")
    public void shouldThrowExceptionWhenWorkoutNotFound() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutExerciseService.addExerciseToWorkout(1L, 1L, requestDTO));
    }

    @Test
    @DisplayName("Should throw NotFoundException when exercise not found")
    public void shouldThrowExceptionWhenExerciseNotFound() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutExerciseService.addExerciseToWorkout(1L, 1L, requestDTO));
    }

    @Test
    @DisplayName("Should throw WorkoutExercisePositionInvalidException when position is already taken")
    public void shouldThrowExceptionWhenPositionAlreadyTaken() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workoutEntity));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exerciseEntity));
        when(workoutExerciseRepository.existsByPosition(1)).thenReturn(true);

        assertThrows(WorkoutExercisePositionInvalidException.class, () -> workoutExerciseService.addExerciseToWorkout(1L, 1L, requestDTO));
    }

    @Test
    @DisplayName("Should find exercises by workout id successfully")
    public void shouldFindExercisesByWorkoutIdSuccessfully() throws NotFoundException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WorkoutExerciseEntity> page = new PageImpl<>(List.of(workoutExerciseEntity));

        when(workoutRepository.existsById(1L)).thenReturn(true);
        when(workoutExerciseRepository.findAllByWorkout_WorkoutId(pageable, 1L)).thenReturn(page);

        Page<WorkoutExerciseResponseDTO> response = workoutExerciseService.findExercisesByWorkoutId(1L, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    @DisplayName("Should throw NotFoundException when listing exercises for non-existent workout")
    public void shouldThrowExceptionWhenListingForNonExistentWorkout() {
        Pageable pageable = PageRequest.of(0, 10);
        when(workoutRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> workoutExerciseService.findExercisesByWorkoutId(1L, pageable));
    }

    @Test
    @DisplayName("Should remove exercise from workout successfully")
    public void shouldRemoveExerciseFromWorkoutSuccessfully() throws NotFoundException {
        WorkoutExerciseId id = new WorkoutExerciseId(1L, 1L);
        when(workoutExerciseRepository.existsById(id)).thenReturn(true);

        workoutExerciseService.removeExerciseFromWorkout(1L, 1L);

        verify(workoutExerciseRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw NotFoundException when removing non-existent workout exercise")
    public void shouldThrowExceptionWhenRemovingNonExistentWorkoutExercise() {
        WorkoutExerciseId id = new WorkoutExerciseId(1L, 1L);
        when(workoutExerciseRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> workoutExerciseService.removeExerciseFromWorkout(1L, 1L));
        verify(workoutExerciseRepository, never()).deleteById(any(WorkoutExerciseId.class));
    }
}
