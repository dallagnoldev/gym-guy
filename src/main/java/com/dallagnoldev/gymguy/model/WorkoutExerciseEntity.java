package com.dallagnoldev.gymguy.model;

import com.dallagnoldev.gymguy.model.embeddable.WorkoutExerciseId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_workout_exercise")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class WorkoutExerciseEntity {

    @EmbeddedId
    private WorkoutExerciseId id;

    @ManyToOne
    @MapsId("workoutId")
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;


    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @Column(precision = 10, scale = 2)
    private BigDecimal weight;
    private Integer reps;
    private Integer sets;
    private Integer position;
}
