package com.dallagnoldev.gymguy.repository;

import com.dallagnoldev.gymguy.model.WorkoutExerciseEntity;
import com.dallagnoldev.gymguy.model.embeddable.WorkoutExerciseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, WorkoutExerciseId> {
}
