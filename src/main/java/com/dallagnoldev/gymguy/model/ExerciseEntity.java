package com.dallagnoldev.gymguy.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_exercise")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode

public class ExerciseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long exerciseid;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "muscular_group")
    private String muscularGroup;
}
