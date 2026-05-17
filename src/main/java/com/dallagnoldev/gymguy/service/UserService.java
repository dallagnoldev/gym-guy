package com.dallagnoldev.gymguy.service;

import com.dallagnoldev.gymguy.dto.UserRequestDTO;
import com.dallagnoldev.gymguy.dto.UserResponseDTO;
import com.dallagnoldev.gymguy.dto.update.UserUpdateRequestDTO;
import com.dallagnoldev.gymguy.exception.EmailAlreadyExistsException;
import com.dallagnoldev.gymguy.exception.NotFoundException;
import com.dallagnoldev.gymguy.exception.PasswordInvalidException;
import com.dallagnoldev.gymguy.model.UserEntity;
import com.dallagnoldev.gymguy.model.enums.UserPlanTypeEnum;
import com.dallagnoldev.gymguy.repository.IUserRepository;
import com.dallagnoldev.gymguy.util.PasswordHelper;
import com.dallagnoldev.gymguy.util.PasswordResponseValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws EmailAlreadyExistsException, PasswordInvalidException {

        if (userRepository.existsByEmail(userRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        PasswordResponseValidation responseValidation = PasswordHelper.validatePassword(userRequestDTO.password());

        if (!responseValidation.isValid()) {
            throw new PasswordInvalidException(responseValidation.message());
        }

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
    public UserResponseDTO findUserById(Long userId) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return toResponse(userEntity);
    }

    @Transactional
    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO) throws NotFoundException, EmailAlreadyExistsException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        if (userUpdateRequestDTO.firstName() != null) {
            userEntity.setFirstName(userUpdateRequestDTO.firstName());
        }

        if (userUpdateRequestDTO.lastName() != null) {
            userEntity.setLastName(userUpdateRequestDTO.lastName());
        }
        if (userUpdateRequestDTO.email() != null) {
            if (!userEntity.getEmail().equals(userUpdateRequestDTO.email()) && userRepository.existsByEmail(userUpdateRequestDTO.email())) {
                throw new EmailAlreadyExistsException("Email already in use");
            }
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
    public void upgradeUserPlan(Long userId) throws NotFoundException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setPlanType(UserPlanTypeEnum.PREMIUM);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
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
                userEntity.getPlanType(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }
}
