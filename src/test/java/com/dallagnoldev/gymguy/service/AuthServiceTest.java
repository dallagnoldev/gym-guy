package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.config.TokenProvider;
import com.dallagnoldev.gymguy.dto.LoginRequestDTO;
import com.dallagnoldev.gymguy.dto.LoginResponseDTO;
import com.dallagnoldev.gymguy.dto.RegisterRequestDTO;
import com.dallagnoldev.gymguy.dto.RegisterResponseDTO;
import com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException;
import com.dallagnoldev.gymguy.exception.PasswordInvalidException;
import com.dallagnoldev.gymguy.model.RolesEntity;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.model.enums.UserSexEnum;
import com.dallagnoldev.gymguy.repository.IRolesRepository;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRolesRepository rolesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        registerRequestDTO = new RegisterRequestDTO(
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

        loginRequestDTO = new LoginRequestDTO("test@gmail.com", "Password123@");

        authentication = mock(Authentication.class);
    }

    @Test
    public void shouldRegisterUserSuccessfully() throws EmailAlreadyExistsException, PasswordInvalidException {
        when(userRepository.existsByEmail(registerRequestDTO.email())).thenReturn(false);

        RolesEntity rolesEntity = RolesEntity.builder()
                .name("ROLE_USER")
                .build();

        when(rolesRepository.findByName(anyString())).thenReturn(Optional.of(rolesEntity));

        when(passwordEncoder.encode(anyString())).thenReturn("hashed-password");

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponseDTO response = authenticationService.register(registerRequestDTO);

        assertNotNull(response);
        assertEquals(registerRequestDTO.email(), response.email());

        verify(userRepository, times(1)).existsByEmail(registerRequestDTO.email());
        verify(rolesRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void shouldLoginUserSuccessfully() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(tokenProvider.generateToken(authentication))
                .thenReturn("token");

        LoginResponseDTO response = authenticationService.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals("token", response.token());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    public void shouldReturnBadCredentialsException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid Credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(loginRequestDTO));
    }
}
