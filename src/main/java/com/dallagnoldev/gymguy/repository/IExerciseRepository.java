package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
}
