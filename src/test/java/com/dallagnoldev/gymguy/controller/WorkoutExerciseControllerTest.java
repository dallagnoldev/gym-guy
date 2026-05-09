package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.WorkoutExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.WorkoutExerciseResponseDTO;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.WorkoutExercisePositionInvalidException;
import com.dallagnoldev.gymguy.service.WorkoutExerciseService;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutExerciseController.class)
public class WorkoutExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutExerciseService workoutExerciseService;

    private WorkoutExerciseRequestDTO workoutExerciseRequestDTO;
    private WorkoutExerciseResponseDTO workoutExerciseResponseDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        workoutExerciseRequestDTO = new WorkoutExerciseRequestDTO(
                new BigDecimal("50.0"),
                10,
                3,
                1
        );

        workoutExerciseResponseDTO = new WorkoutExerciseResponseDTO(
                1L,
                "Bench Press",
                new BigDecimal("50.0"),
                10,
                3,
                1,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    public void shouldAddExerciseToWorkoutSuccessfully() throws Exception {
        when(workoutExerciseService.addExerciseToWorkout(eq(1L), eq(1L), any(WorkoutExerciseRequestDTO.class)))
                .thenReturn(workoutExerciseResponseDTO);

        mockMvc.perform(post("/api/v1/workouts/{workoutId}/exercises/{exerciseId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutExerciseRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.exerciseId").value(1))
                .andExpect(jsonPath("$.exerciseName").value("Bench Press"));

        verify(workoutExerciseService, times(1)).addExerciseToWorkout(eq(1L), eq(1L), any(WorkoutExerciseRequestDTO.class));
    }

    @Test
    public void shouldReturnNotFoundWhenWorkoutOrExerciseDoesNotExistOnAdd() throws Exception {
        when(workoutExerciseService.addExerciseToWorkout(eq(1L), eq(1L), any(WorkoutExerciseRequestDTO.class)))
                .thenThrow(new NotFoundException("Workout or Exercise not found"));

        mockMvc.perform(post("/api/v1/workouts/{workoutId}/exercises/{exerciseId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutExerciseRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Workout or Exercise not found"));
    }

    @Test
    public void shouldReturnConflictWhenPositionIsInvalidOnAdd() throws Exception {
        when(workoutExerciseService.addExerciseToWorkout(eq(1L), eq(1L), any(WorkoutExerciseRequestDTO.class)))
                .thenThrow(new WorkoutExercisePositionInvalidException("Position is already taken"));

        mockMvc.perform(post("/api/v1/workouts/{workoutId}/exercises/{exerciseId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workoutExerciseRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Position is already taken"));
    }

    @Test
    public void shouldFindExercisesByWorkoutSuccessfully() throws Exception {
        Page<WorkoutExerciseResponseDTO> page = new PageImpl<>(List.of(workoutExerciseResponseDTO));
        when(workoutExerciseService.findExercisesByWorkoutId(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/workouts/{workoutId}/exercises", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].exerciseId").value(1))
                .andExpect(jsonPath("$.content[0].exerciseName").value("Bench Press"));

        verify(workoutExerciseService, times(1)).findExercisesByWorkoutId(eq(1L), any(Pageable.class));
    }

    @Test
    public void shouldReturnNotFoundWhenWorkoutDoesNotExistOnFindExercises() throws Exception {
        when(workoutExerciseService.findExercisesByWorkoutId(eq(1L), any(Pageable.class)))
                .thenThrow(new NotFoundException("Workout not found"));

        mockMvc.perform(get("/api/v1/workouts/{workoutId}/exercises", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Workout not found"));
    }

    @Test
    public void shouldRemoveExerciseFromWorkoutSuccessfully() throws Exception {
        doNothing().when(workoutExerciseService).removeExerciseFromWorkout(1L, 1L);

        mockMvc.perform(delete("/api/v1/workouts/{workoutId}/exercises/{exerciseId}", 1L, 1L))
                .andExpect(status().isNoContent());

        verify(workoutExerciseService, times(1)).removeExerciseFromWorkout(1L, 1L);
    }

    @Test
    public void shouldReturnNotFoundWhenWorkoutOrExerciseDoesNotExistOnRemove() throws Exception {
        doThrow(new NotFoundException("Workout or Exercise not found")).when(workoutExerciseService).removeExerciseFromWorkout(1L, 1L);

        mockMvc.perform(delete("/api/v1/workouts/{workoutId}/exercises/{exerciseId}", 1L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Workout or Exercise not found"));
    }
}
