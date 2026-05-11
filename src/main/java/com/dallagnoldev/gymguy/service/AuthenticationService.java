package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.config.JwtAuthenticationFilter;
import com.dallagnoldev.gymguy.config.TokenProvider;
import com.dallagnoldev.gymguy.dto.LoginRequestDTO;
import com.dallagnoldev.gymguy.dto.LoginResponseDTO;
import com.dallagnoldev.gymguy.dto.RegisterRequestDTO;
import com.dallagnoldev.gymguy.dto.RegisterResponseDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private Long expirationTime;

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) throws EmailAlreadyExistsException, PasswordInvalidException {

        if (userRepository.existsByEmail(registerRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        PasswordResponseValidation responseValidation = PasswordHelper.validatePassword(registerRequestDTO.password());

        if (!responseValidation.isValid()) {
            throw new PasswordInvalidException(responseValidation.message());
        }

        RolesEntity role = rolesRepository.findByName(RoleTypeEnum.ROLE_USER.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                                .name(RoleTypeEnum.ROLE_USER.name())
                        .build()));

        UserEntity user = UserEntity.builder()
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
                        .build();

        return toResponse(userRepository.save(user));

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
            String token = tokenProvider.generateToken(authentication);

            return new LoginResponseDTO(token, expirationTime);

        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException(ex.getMessage());
        } catch (Exception e) {
            throw new Exception("Internal Error", e);
        }
    }

    public RegisterResponseDTO toResponse(UserEntity userEntity) {
        return new RegisterResponseDTO(
                userEntity.getUserId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail()
        );
    }
}
