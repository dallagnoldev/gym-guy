package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.dto.WorkoutResponseDTO;
import com.dallagnoldev.gymguy.model.WorkoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

    Page<WorkoutEntity> findAllWorkoutsByUserId_UserId(Long userId, Pageable pageable);
}
