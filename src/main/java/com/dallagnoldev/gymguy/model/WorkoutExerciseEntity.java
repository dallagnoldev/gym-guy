package com.dallagnoldev.gymguy.model;

import com.dallagnoldev.gymguy.model.embeddable.WorkoutExerciseId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

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

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
