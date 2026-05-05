ALTER TABLE tb_workout_exercise
ADD CONSTRAINT uk_workout_position UNIQUE (workout_id, position)