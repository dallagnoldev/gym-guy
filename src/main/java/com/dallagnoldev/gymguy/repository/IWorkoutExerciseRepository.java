package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.WorkoutExerciseEntity;
import com.dallagnoldev.gymguy.model.embeddable.WorkoutExerciseId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, WorkoutExerciseId> {

    Page<WorkoutExerciseEntity> findAllByWorkout_WorkoutId(Pageable pageable, Long workoutId);

    boolean existsByPosition(Integer position);
}
