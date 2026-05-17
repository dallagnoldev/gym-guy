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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private IWorkoutRepository workoutRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private WorkoutExerciseService workoutExerciseService;

    private WorkoutRequestDTO workoutRequestDTO;
    private UserEntity userEntity;
    private WorkoutEntity workoutEntity;

    @BeforeEach
    public void setUp() {
        workoutRequestDTO = new WorkoutRequestDTO(
                "Full Body A",
                "Intermediate workout"
        );

        userEntity = UserEntity.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        workoutEntity = WorkoutEntity.builder()
                .workoutId(1L)
                .name(workoutRequestDTO.name())
                .description(workoutRequestDTO.description())
                .userId(userEntity)
                .exercises(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Should create workout successfully")
    public void shouldCreateWorkoutSuccessfully() throws NotFoundException, WorkoutNameMustBeUniqueException, QuantityLimitException {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(workoutRepository.existsByNameIgnoreCase(workoutRequestDTO.name())).thenReturn(false);
        when(workoutRepository.countAllWorkoutsByUserId_UserId(userId)).thenReturn(5L);
        when(workoutRepository.save(any(WorkoutEntity.class))).thenReturn(workoutEntity);

        WorkoutResponseDTO response = workoutService.createWorkout(userId, workoutRequestDTO);

        assertNotNull(response);
        assertEquals(workoutRequestDTO.name(), response.name());
        verify(workoutRepository, times(1)).save(any(WorkoutEntity.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when user not found during workout creation")
    public void shouldThrowExceptionWhenUserNotFoundOnCreation() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutService.createWorkout(userId, workoutRequestDTO));
        verify(workoutRepository, never()).save(any(WorkoutEntity.class));
    }

    @Test
    @DisplayName("Should throw WorkoutNameMustBeUniqueException when workout name already exists")
    public void shouldThrowExceptionWhenDuplicateWorkoutName() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(workoutRepository.existsByNameIgnoreCase(workoutRequestDTO.name())).thenReturn(true);

        assertThrows(WorkoutNameMustBeUniqueException.class, () -> workoutService.createWorkout(userId, workoutRequestDTO));
    }

    @Test
    @DisplayName("Should throw WorkoutQuantityLimitException when limit is reached")
    public void shouldThrowExceptionWhenWorkoutLimitReached() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(workoutRepository.existsByNameIgnoreCase(workoutRequestDTO.name())).thenReturn(false);
        when(workoutRepository.countAllWorkoutsByUserId_UserId(userId)).thenReturn(10L);

        assertThrows(QuantityLimitException.class, () -> workoutService.createWorkout(userId, workoutRequestDTO));
    }

    @Test
    @DisplayName("Should find workout by id successfully")
    public void shouldFindWorkoutByIdSuccessfully() throws NotFoundException {
        Long id = 1L;
        when(workoutRepository.findById(id)).thenReturn(Optional.of(workoutEntity));

        WorkoutResponseDTO response = workoutService.findWorkoutById(id);

        assertNotNull(response);
        assertEquals(id, response.workoutId());
    }

    @Test
    @DisplayName("Should throw NotFoundException when workout id not found")
    public void shouldThrowExceptionWhenWorkoutNotFound() {
        Long id = 1L;
        when(workoutRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutService.findWorkoutById(id));
    }

    @Test
    @DisplayName("Should find all workouts by user id successfully")
    public void shouldFindAllWorkoutsByUserIdSuccessfully() throws NotFoundException {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<WorkoutEntity> page = new PageImpl<>(List.of(workoutEntity));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(workoutRepository.findAllWorkoutsByUserId_UserId(userId, pageable)).thenReturn(page);

        Page<WorkoutResponseDTO> response = workoutService.findAllWorkoutsByUserId(pageable, userId);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    @DisplayName("Should throw NotFoundException when finding workouts for non-existent user")
    public void shouldThrowExceptionWhenUserNotFoundOnListing() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> workoutService.findAllWorkoutsByUserId(pageable, userId));
    }

    @Test
    @DisplayName("Should delete workout successfully")
    public void shouldDeleteWorkoutSuccessfully() throws NotFoundException {
        Long id = 1L;
        when(workoutRepository.existsById(id)).thenReturn(true);

        workoutService.deleteWorkout(id);

        verify(workoutRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existent workout")
    public void shouldThrowExceptionWhenDeletingNonExistentWorkout() {
        Long id = 1L;
        when(workoutRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> workoutService.deleteWorkout(id));
        verify(workoutRepository, never()).deleteById(anyLong());
    }
}
