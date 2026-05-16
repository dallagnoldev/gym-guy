ALTER TABLE tb_exercise ADD COLUMN user_id bigint;
ALTER TABLE tb_exercise ADD CONSTRAINT fk_exercise_user foreign key (user_id) references tb_user(user_id);