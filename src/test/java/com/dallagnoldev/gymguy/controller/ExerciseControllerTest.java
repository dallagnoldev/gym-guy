package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.ExerciseRequestDTO;
import com.dallagnoldev.gymguy.dto.ExerciseResponseDTO;
import com.dallagnoldev.gymguy.service.ExerciseService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectmapper;

    @MockitoBean
    private ExerciseService exerciseService;

    private ExerciseRequestDTO exerciseRequestDTO;

    @BeforeEach
    public void setUp() {
        objectmapper = new ObjectMapper();
        objectmapper.registerModule(new JavaTimeModule());

        exerciseRequestDTO = new ExerciseRequestDTO(
                "Bench Press",
                "Chest"
        );
    }

    @Test
    public void shouldCreateExerciseSuccessfully() throws Exception {
        ExerciseResponseDTO response = new ExerciseResponseDTO(
                1L,
                exerciseRequestDTO.name(),
                exerciseRequestDTO.muscularGroup(),
                null,
                null
        );

        when(exerciseService.createExercise(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectmapper.writeValueAsString(exerciseRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.exerciseId").value(1))
                .andExpect(jsonPath("$.name").value(exerciseRequestDTO.name()));

        verify(exerciseService, times(1)).createExercise(any());
    }

    @Test
    public void shouldFindExerciseByIdSuccessfully() throws Exception {
        ExerciseResponseDTO response = new ExerciseResponseDTO(
                1L,
                exerciseRequestDTO.name(),
                exerciseRequestDTO.muscularGroup(),
                null,
                null
        );

        when(exerciseService.findExerciseById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/exercises/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseId").value(1))
                .andExpect(jsonPath("$.name").value(exerciseRequestDTO.name()));

        verify(exerciseService, times(1)).findExerciseById(1L);
    }

    @Test
    public void shouldFindAllExercisesSuccessfully() throws Exception {
        ExerciseResponseDTO response = new ExerciseResponseDTO(
                1L,
                exerciseRequestDTO.name(),
                exerciseRequestDTO.muscularGroup(),
                null,
                null
        );

        Page<ExerciseResponseDTO> responsePage = new PageImpl<>(List.of(response));

        when(exerciseService.findAllExercises(any(Pageable.class))).thenReturn(responsePage);

        mockMvc.perform(get("/api/v1/exercises")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].exerciseId").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Bench Press"));

        verify(exerciseService, times(1)).findAllExercises(any(Pageable.class));
    }

    @Test
    public void shouldFindExerciseByNameSuccessfully() throws Exception {
        String exerciseName = "Bench Press";

        ExerciseResponseDTO response = new ExerciseResponseDTO(
                1L,
                exerciseRequestDTO.name(),
                exerciseRequestDTO.muscularGroup(),
                null,
                null
        );

        when(exerciseService.findExerciseByName(exerciseName)).thenReturn(response);

        mockMvc.perform(get("/api/v1/exercises/search")
                        .param("name", "Bench Press")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.exerciseId").value(1))
                        .andExpect(jsonPath("$.name").value(exerciseRequestDTO.name()));

        verify(exerciseService, times(1)).findExerciseByName(exerciseName);

    }

    @Test
    public void shouldFindExerciseByMuscularGroupSuccessfully() throws Exception {
        String muscularGroup = "Chest";

        ExerciseResponseDTO response = new ExerciseResponseDTO(
                1L,
                exerciseRequestDTO.name(),
                muscularGroup,
                null,
                null
        );

        Page<ExerciseResponseDTO> responsePage = new PageImpl<>(List.of(response));

        when(exerciseService.findExercisesByMuscularGroup(any(Pageable.class), eq(muscularGroup))).thenReturn(responsePage);

        mockMvc.perform(get("/api/v1/exercises/muscular-group")
                .param("group", muscularGroup)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].exerciseId").value(1))
                .andExpect(jsonPath("$.content[0].name").value(exerciseRequestDTO.name()))
                .andExpect(jsonPath("$.content[0].muscularGroup").value(muscularGroup));

        verify(exerciseService, times(1)).findExercisesByMuscularGroup(any(Pageable.class), eq(muscularGroup));
    }

    @Test
    public void shouldDeleteExerciseSuccessfully() throws Exception {
        doNothing().when(exerciseService).deleteExercise(1L);

        mockMvc.perform(delete("/api/v1/exercises/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(exerciseService, times(1)).deleteExercise(1L);
    }
}
