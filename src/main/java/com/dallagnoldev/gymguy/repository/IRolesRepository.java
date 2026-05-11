package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolesRepository extends JpaRepository<RolesEntity, Long> {
}
