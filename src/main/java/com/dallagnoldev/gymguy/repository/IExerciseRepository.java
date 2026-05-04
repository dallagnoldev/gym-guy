package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    Optional<ExerciseEntity> findByName(String name);

    List<ExerciseEntity> findByMuscularGroup(String muscularGroup);
}
