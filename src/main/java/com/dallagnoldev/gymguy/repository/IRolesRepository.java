package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRolesRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(String role);
}
