package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.config.TokenProvider;
import com.dallagnoldev.gymguy.dto.LoginRequestDTO;
import com.dallagnoldev.gymguy.dto.LoginResponseDTO;
import com.dallagnoldev.gymguy.dto.RegisterRequestDTO;
import com.dallagnoldev.gymguy.dto.RegisterResponseDTO;
import com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException;
import com.dallagnoldev.gymguy.exception.PasswordInvalidException;
import com.dallagnoldev.gymguy.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register (@RequestBody @Valid RegisterRequestDTO registerRequestDTO) throws EmailAlreadyExistsException, PasswordInvalidException {
        RegisterResponseDTO responseDTO = authenticationService.register(registerRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.userId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody @Valid LoginRequestDTO loginRequestDTO) throws Exception {
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO));
    }
}
