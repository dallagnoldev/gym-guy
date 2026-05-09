package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.WorkoutRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.WorkoutNameMustBeUniqueException;
import com.dallagnoldev.gymguy.exception.WorkoutQuantityLimitException;
import com.dallagnoldev.gymguy.service.WorkoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutService workoutService;

    private WorkoutRequestDTO workoutRequestDTO;
    private WorkoutResponseDTO workoutResponseDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        workoutRequestDTO = new WorkoutRequestDTO("Upper Body", "Chest and Back focus");
        workoutResponseDTO = new WorkoutResponseDTO(
                1L,
                "Upper Body",
                "Chest and Back focus",
                List.of(),
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    public void shouldCreateWorkoutSuccessfully() throws Exception {
        when(workoutService.createWorkout(eq(1L), any(WorkoutRequestDTO.class))).thenReturn(workoutResponseDTO);

        mockMvc.perform(post("/api/v1/users/{userId}/workouts", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.workoutId").value(1))
                .andExpect(jsonPath("$.name").value("Upper Body"));

        verify(workoutService, times(1)).createWorkout(eq(1L), any(WorkoutRequestDTO.class));
    }

    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExistOnCreate() throws Exception {
        when(workoutService.createWorkout(eq(1L), any(WorkoutRequestDTO.class))).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(post("/api/v1/users/{userId}/workouts", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    public void shouldReturnConflictWhenWorkoutNameAlreadyExists() throws Exception {
        when(workoutService.createWorkout(eq(1L), any(WorkoutRequestDTO.class))).thenThrow(new WorkoutNameMustBeUniqueException("Workout name must be unique"));

        mockMvc.perform(post("/api/v1/users/{userId}/workouts", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Workout name must be unique"));
    }

    @Test
    public void shouldReturnConflictWhenWorkoutLimitReached() throws Exception {
        when(workoutService.createWorkout(eq(1L), any(WorkoutRequestDTO.class))).thenThrow(new WorkoutQuantityLimitException("Workout quantity limit reached"));

        mockMvc.perform(post("/api/v1/users/{userId}/workouts", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Workout quantity limit reached"));
    }

    @Test
    public void shouldFindAllWorkoutsByUserSuccessfully() throws Exception {
        Page<WorkoutResponseDTO> page = new PageImpl<>(List.of(workoutResponseDTO));
        when(workoutService.findAllWorkoutsByUserId(any(Pageable.class), eq(1L))).thenReturn(page);

        mockMvc.perform(get("/api/v1/users/{userId}/workouts", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].workoutId").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Upper Body"));

        verify(workoutService, times(1)).findAllWorkoutsByUserId(any(Pageable.class), eq(1L));
    }

    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExistOnFindAll() throws Exception {
        when(workoutService.findAllWorkoutsByUserId(any(Pageable.class), eq(1L))).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/{userId}/workouts", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    public void shouldFindWorkoutByIdSuccessfully() throws Exception {
        when(workoutService.findWorkoutById(1L)).thenReturn(workoutResponseDTO);

        mockMvc.perform(get("/api/v1/users/{userId}/workouts/{workoutId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutId").value(1))
                .andExpect(jsonPath("$.name").value("Upper Body"));

        verify(workoutService, times(1)).findWorkoutById(1L);
    }

    @Test
    public void shouldReturnNotFoundWhenWorkoutDoesNotExistOnFindById() throws Exception {
        when(workoutService.findWorkoutById(1L)).thenThrow(new NotFoundException("Workout not found"));

        mockMvc.perform(get("/api/v1/users/{userId}/workouts/{workoutId}", 1L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Workout not found"));
    }

    @Test
    public void shouldDeleteWorkoutSuccessfully() throws Exception {
        doNothing().when(workoutService).deleteWorkout(1L);

        mockMvc.perform(delete("/api/v1/users/{userId}/workouts/{workoutId}", 1L, 1L))
                .andExpect(status().isNoContent());

        verify(workoutService, times(1)).deleteWorkout(1L);
    }

    @Test
    public void shouldReturnNotFoundWhenWorkoutDoesNotExistOnDelete() throws Exception {
        doThrow(new NotFoundException("Workout not found")).when(workoutService).deleteWorkout(1L);

        mockMvc.perform(delete("/api/v1/users/{userId}/workouts/{workoutId}", 1L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Workout not found"));
    }
}
