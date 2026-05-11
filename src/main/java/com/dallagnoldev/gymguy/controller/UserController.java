package com.dallagnoldev.gymguy.controller;

import com.dallagnoldev.gymguy.dto.UserRequestDTO;
import com.dallagnoldev.gymguy.dto.UserResponseDTO;
import com.dallagnoldev.gymguy.dto.update.UserUpdateRequestDTO;
import com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.PasswordInvalidException;
import com.dallagnoldev.gymguy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createExercise(@RequestBody @Valid UserRequestDTO userRequestDTO) throws EmailAlreadyExistsException, PasswordInvalidException {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponseDTO.userId())
                .toUri();

        return ResponseEntity.created(location).body(userResponseDTO);
    }

    @PreAuthorize("#userId  == authentication.principal.userId or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long userId) throws NotFoundException {
        UserResponseDTO userResponseDTO = userService.findUserById(userId);

        return ResponseEntity.ok(userResponseDTO);
    }

    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateRequestDTO userUpdateRequestDTO) throws NotFoundException, EmailAlreadyExistsException {
        UserResponseDTO userResponseDTO = userService.updateUser(userId, userUpdateRequestDTO);

        return ResponseEntity.ok(userResponseDTO);
    }

    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?>  deleteUser(@PathVariable Long userId) throws NotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
