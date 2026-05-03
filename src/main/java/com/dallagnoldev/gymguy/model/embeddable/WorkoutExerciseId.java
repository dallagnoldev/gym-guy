package com.dallagnoldev.gymguy.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode

@Embeddable
public class WorkoutExerciseId implements Serializable {
    private Long workoutId;
    private Long exerciseId;
}
