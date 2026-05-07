package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.UserRequestDTO;
import com.dallagnoldev.gymguy.dto.UserResponseDTO;
import com.dallagnoldev.gymguy.dto.update.UserUpdateRequestDTO;
import com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.PasswordInvalidException;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.model.enums.UserSexEnum;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository  userRepository;

    private UserRequestDTO userRequestDTO;

    @BeforeEach
    public void setUp() {
        userRequestDTO = new UserRequestDTO(
                "Angelica",
                "Ferreira",
                "angelica@gmail.com",
                "Password123@",
                "40028922",
                LocalDate.of(2002,
                        12,
                        18),
                UserSexEnum.M,
                1.65,
                62.5
        );
    }

    @Test
    public void shouldCreateUserSuccessfully() throws EmailAlreadyExistsException, PasswordInvalidException {
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(false);

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.createUser(userRequestDTO);

        assertNotNull(response);
        assertEquals(userRequestDTO.email(), response.email());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExistsException() throws EmailAlreadyExistsException {
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(userRequestDTO);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldFindUserByIdSuccessfully() throws NotFoundException {
        Long userId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .firstName("Angelica")
                .lastName("Ferreira")
                .email("angelica@gmail.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserResponseDTO result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals("Angelica", result.firstName());
        assertEquals("Ferreira", result.lastName());
        assertEquals(userId,  result.userId());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 99L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.findUserById(id);
        });

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void shouldUpdatedUserSuccessfully() throws NotFoundException, EmailAlreadyExistsException {
        Long id = 1L;

        UserEntity oldData = UserEntity.builder()
                .userId(id)
                .firstName("Angelica")
                .email("angelica@gmail.com")
                .weight(60.0)
                .build();

        UserUpdateRequestDTO newData =  new UserUpdateRequestDTO(
                "Angelica",
                "Ferreira",
                "angelica_new@gmail.com",
                "40028922",
                LocalDate.of(2002, 12, 18),
                UserSexEnum.F,
                1.70,
                65.0

        );

        when(userRepository.findById(id)).thenReturn(Optional.of(oldData));
        when(userRepository.existsByEmail("angelica_new@gmail.com")).thenReturn(false);
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.updateUser(id, newData);

        assertNotNull(response);
        assertEquals(id,  response.userId());
        assertEquals("angelica_new@gmail.com", response.email());
        assertEquals(65.0, response.weight());
        assertEquals(1.70, response.height());

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByEmail("angelica_new@gmail.com");
        verify(userRepository, times(1)).saveAndFlush(any(UserEntity.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingToExistingEmail() {
        Long id = 1L;
        String existingEmail = "other@gmail.com";
        UserUpdateRequestDTO newData = new UserUpdateRequestDTO(
                null, null, existingEmail, null, null, null, null, null
        );

        UserEntity oldData = UserEntity.builder().userId(id).email("old@gmail.com").build();

        when(userRepository.findById(id)).thenReturn(Optional.of(oldData));
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.updateUser(id, newData);
        });

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByEmail(existingEmail);
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsInvalid() {
        UserRequestDTO invalidPasswordDTO = new UserRequestDTO(
                "Angelica", "Ferreira", "angelica@gmail.com",
                "123", "40028922", LocalDate.of(2002, 12, 18),
                UserSexEnum.M, 1.65, 62.5
        );

        when(userRepository.existsByEmail(invalidPasswordDTO.email())).thenReturn(false);

        assertThrows(PasswordInvalidException.class, () -> {
            userService.createUser(invalidPasswordDTO);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        Long id = 99L;
        UserUpdateRequestDTO newData = new UserUpdateRequestDTO(
                "New Name", null, null, null, null, null, null, null
        );

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(id, newData);
        });

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    public void shouldDeleteUserSuccessfully() throws NotFoundException {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentUser() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(userId);
        });

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
