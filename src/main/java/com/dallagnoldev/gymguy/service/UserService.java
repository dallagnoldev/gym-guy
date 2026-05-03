package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.UserRequestDTO;
import com.dallagnoldev.gymguy.dto.UserResponseDTO;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = UserEntity.builder()
                .firstName(userRequestDTO.firstName())
                .lastName(userRequestDTO.lastName())
                .email(userRequestDTO.email())
                .password(userRequestDTO.password())
                .phoneNumber(userRequestDTO.phoneNumber())
                .sex(userRequestDTO.sex())
                .height(userRequestDTO.height())
                .weight(userRequestDTO.weight())
                .build();

        return toResponse(userRepository.save(userEntity));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toResponse(userEntity);
    }

    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userRequestDTO.firstName() != null) {
            userEntity.setFirstName(userRequestDTO.firstName());
        }

        if (userRequestDTO.lastName() != null) {
            userEntity.setLastName(userRequestDTO.lastName());
        }
        if (userRequestDTO.email() != null) {
            userEntity.setEmail(userRequestDTO.email());
        }
        if (userRequestDTO.phoneNumber() != null) {
            userEntity.setPhoneNumber(userRequestDTO.phoneNumber());
        }
        if (userRequestDTO.sex() != null) {
            userEntity.setSex(userRequestDTO.sex());
        }

        if (userRequestDTO.height() != null) {
            userEntity.setHeight(userRequestDTO.height());
        }

        if (userRequestDTO.weight() != null) {
            userEntity.setWeight(userRequestDTO.weight());
        }

        return  toResponse(userRepository.saveAndFlush(userEntity));
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        userRepository.deleteById(userId);
    }

    public UserResponseDTO toResponse(UserEntity userEntity) {
        return new UserResponseDTO(
                userEntity.getUserId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPhoneNumber(),
                userEntity.getSex(),
                userEntity.getHeight(),
                userEntity.getWeight()
        );
    }
}
