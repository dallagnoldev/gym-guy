package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}
