package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.config.JwtAuthenticationFilter;
import com.dallagnoldev.gymguy.dto.RegisterRequestDTO;
import com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException;
import com.dallagnoldev.gymguy.exception.PasswordInvalidException;
import com.dallagnoldev.gymguy.model.RolesEntity;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.model.enums.RoleTypeEnum;
import com.dallagnoldev.gymguy.repository.IRolesRepository;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import com.dallagnoldev.gymguy.util.PasswordHelper;
import com.dallagnoldev.gymguy.util.PasswordResponseValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRolesRepository rolesRepository;

    public void register(RegisterRequestDTO registerRequestDTO) throws EmailAlreadyExistsException, PasswordInvalidException {

        if (userRepository.existsByEmail(registerRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        PasswordResponseValidation responseValidation = PasswordHelper.validatePassword(registerRequestDTO.password());

        if (!responseValidation.isValid()) {
            throw new PasswordInvalidException(responseValidation.message());
        }

        RolesEntity role = rolesRepository.findByName(RoleTypeEnum.USER.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                                .name(RoleTypeEnum.USER.name())
                        .build()));

        userRepository.save(UserEntity.builder()
                        .firstName(registerRequestDTO.firstName())
                        .lastName(registerRequestDTO.lastName())
                        .email(registerRequestDTO.email())
                        .password(passwordEncoder.encode(registerRequestDTO.password()))
                        .phoneNumber(registerRequestDTO.phoneNumber())
                        .birthDate(registerRequestDTO.birthDate())
                        .sex(registerRequestDTO.sex())
                        .height(registerRequestDTO.height())
                        .weight(registerRequestDTO.weight())
                        .roles(Set.of(role))
                        .build());


    }
}
