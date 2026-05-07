package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.ExerciseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    Optional<ExerciseEntity> findByNameIgnoreCase(String name);

    Page<ExerciseEntity> findByMuscularGroupIgnoreCase(Pageable pageable, String muscularGroup);

    boolean existsByName(String name);
}
