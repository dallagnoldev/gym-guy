package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
}
