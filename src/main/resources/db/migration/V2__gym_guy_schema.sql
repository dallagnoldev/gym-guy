CREATE TABLE tb_workout_exercise(
    workout_id bigint not null,
    exercise_id bigint not null,
    weight decimal not null,
    reps int not null,
    sets int not null,
    position int not null,
    primary key (workout_id, exercise_id),
    foreign key (workout_id) references tb_workout(workout_id) on delete cascade,
    foreign key (exercise_id) references tb_exercise(exercise_id) on delete cascade
);

ALTER TABLE tb_workout add description varchar(255);