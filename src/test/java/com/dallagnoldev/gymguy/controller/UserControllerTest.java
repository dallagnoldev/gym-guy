package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.config.TokenProvider;
import com.dallagnoldev.gymguy.dto.UserRequestDTO;
import com.dallagnoldev.gymguy.dto.UserResponseDTO;
import com.dallagnoldev.gymguy.dto.update.UserUpdateRequestDTO;
import com.dallagnoldev.gymguy.model.enums.UserPlanTypeEnum;
import com.dallagnoldev.gymguy.model.enums.UserSexEnum;
import com.dallagnoldev.gymguy.service.UserService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private UserDetailsService  userDetailsService;

    private UserRequestDTO userRequestDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userRequestDTO = new UserRequestDTO(
                "John",
                "Doe",
                "johndoe@gmail.com",
                "Password123@",
                "40028922",
                LocalDate.of(2002, 10, 2),
                UserSexEnum.M,
                1.77,
                73.0
        );
    }

    @Test
    public void shouldCreateUserSuccessfully() throws Exception {
        UserResponseDTO response = new UserResponseDTO(
                1L,
                userRequestDTO.firstName(),
                userRequestDTO.lastName(),
                userRequestDTO.email(),
                userRequestDTO.phoneNumber(),
                userRequestDTO.birthDate(),
                userRequestDTO.sex(),
                userRequestDTO.height(),
                userRequestDTO.weight(),
                UserPlanTypeEnum.FREE,
                null,
                null
        );

        when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.firstName").value(userRequestDTO.firstName()));

        verify(userService, times(1)).createUser(any());
    }

    @Test
    public void shouldFindUserByIdSuccessfully() throws Exception {
        Long userId = 1L;

        UserResponseDTO response = new UserResponseDTO(
                userId,
                "John",
                "Doe",
                "johndoe@gmail.com",
                "40028922",
                LocalDate.of(2002, 10, 2),
                UserSexEnum.M,
                1.77,
                73.0,
                UserPlanTypeEnum.FREE,
                null,
                null
        );

        when(userService.findUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/{userId}",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.firstName").value(userRequestDTO.firstName()));

        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    public void shouldUpdateUserSuccessfully() throws Exception {
        Long userId = 1L;

        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(
                "James",
                "Bond",
                "jamesbond007@gmail.com",
                "40028922",
                LocalDate.of(2002, 10, 2),
                UserSexEnum.M,
                1.77,
                75.0
        );

        UserResponseDTO response = new UserResponseDTO(
                userId,
                "Johnny",
                userRequestDTO.lastName(),
                userRequestDTO.email(),
                userRequestDTO.phoneNumber(),
                userRequestDTO.birthDate(),
                userRequestDTO.sex(),
                userRequestDTO.height(),
                userRequestDTO.weight(),
                UserPlanTypeEnum.FREE,
                null,
                null
        );

        when(userService.updateUser(eq(userId), any(UserUpdateRequestDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/users/{userId}",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.firstName").value("Johnny"));

        verify(userService, times(1)).updateUser(eq(userId), any(UserUpdateRequestDTO.class));
    }

    @Test
    public void shouldUpgradeUserSuccessfully() throws Exception {
        Long userId = 1L;
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(
                "James",
                "Bond",
                "jamesbond007@gmail.com",
                "40028922",
                LocalDate.of(2002, 10, 2),
                UserSexEnum.M,
                1.77,
                75.0
        );

       mockMvc.perform(patch("/api/v1/users/{userId}/upgrade",userId)
               .content(objectMapper.writeValueAsString(userUpdateRequestDTO)))
               .andExpect(status().isNoContent());

       verify(userService, times(1)).upgradeUserPlan(userId);

    }

    @Test
    public void shouldDeleteUserSuccessfully() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/{userId}",1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        when(userService.createUser(any())).thenThrow(new com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"))
                .andExpect(jsonPath("$.status").value(409));

        verify(userService, times(1)).createUser(any());
    }

    @Test
    public void shouldReturnBadRequestWhenPasswordIsInvalid() throws Exception {
        when(userService.createUser(any())).thenThrow(new com.dallagnoldev.gymguy.exception.PasswordInvalidException("Password invalid"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password invalid"))
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(1)).createUser(any());
    }

    @Test
    public void shouldReturnNotFoundWhenUserIdDoesNotExist() throws Exception {
        when(userService.findUserById(1L)).thenThrow(new com.dallagnoldev.gymguy.exception.NotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/{userId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO("James", "Bond", "james@bond.com", "40028922", LocalDate.of(2002, 10, 2), UserSexEnum.M, 1.77, 75.0);
        when(userService.updateUser(eq(1L), any())).thenThrow(new com.dallagnoldev.gymguy.exception.NotFoundException("User not found"));

        mockMvc.perform(patch("/api/v1/users/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(userService, times(1)).updateUser(eq(1L), any());
    }

    @Test
    public void shouldReturnConflictWhenUpdatingToExistingEmail() throws Exception {
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO("James", "Bond", "existing@email.com", "40028922", LocalDate.of(2002, 10, 2), UserSexEnum.M, 1.77, 75.0);
        when(userService.updateUser(eq(1L), any())).thenThrow(new com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(patch("/api/v1/users/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"))
                .andExpect(jsonPath("$.status").value(409));

        verify(userService, times(1)).updateUser(eq(1L), any());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentUser() throws Exception {
        doThrow(new com.dallagnoldev.gymguy.exception.NotFoundException("User not found")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/{userId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(userService, times(1)).deleteUser(1L);
    }
}
