package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.UserRequestDTO;
import com.dallagnoldev.gymguy.dto.UserResponseDTO;
import com.dallagnoldev.gymguy.dto.update.UserUpdateRequestDTO;
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
                .birthDate(userRequestDTO.birthDate())
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
    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userUpdateRequestDTO.firstName() != null) {
            userEntity.setFirstName(userUpdateRequestDTO.firstName());
        }

        if (userUpdateRequestDTO.lastName() != null) {
            userEntity.setLastName(userUpdateRequestDTO.lastName());
        }
        if (userUpdateRequestDTO.email() != null) {
            userEntity.setEmail(userUpdateRequestDTO.email());
        }
        if (userUpdateRequestDTO.phoneNumber() != null) {
            userEntity.setPhoneNumber(userUpdateRequestDTO.phoneNumber());
        }
        if(userUpdateRequestDTO.birthDate() != null) {
            userEntity.setBirthDate(userUpdateRequestDTO.birthDate());
        }
        if (userUpdateRequestDTO.sex() != null) {
            userEntity.setSex(userUpdateRequestDTO.sex());
        }

        if (userUpdateRequestDTO.height() != null) {
            userEntity.setHeight(userUpdateRequestDTO.height());
        }

        if (userUpdateRequestDTO.weight() != null) {
            userEntity.setWeight(userUpdateRequestDTO.weight());
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
                userEntity.getBirthDate(),
                userEntity.getSex(),
                userEntity.getHeight(),
                userEntity.getWeight(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }
}
