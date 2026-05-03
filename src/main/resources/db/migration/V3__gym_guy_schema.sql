ALTER TABLE tb_user ALTER COLUMN sex TYPE VARCHAR(1);
ALTER TABLE tb_user ADD CONSTRAINT check_sex CHECK (UPPER(sex) IN ('M', 'F'));

ALTER TABLE tb_user ADD COLUMN workout_id bigint;
ALTER TABLE tb_user ADD CONSTRAINT fk_workout_id FOREIGN KEY (workout_id) REFERENCES tb_workout(workout_id);