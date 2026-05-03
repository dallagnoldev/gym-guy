CREATE TABLE tb_user(
    user_id bigserial not null primary key,
    first_name varchar(150) not null,
    last_name varchar(150) not null,
    email varchar(150) not null unique,
    password varchar(150) not null,
    phone_number varchar(30) not null,
    sex char(1) not null,
    height double precision not null,
    weight double precision not null
);

CREATE TABLE tb_exercise(
    exercise_id bigserial not null primary key,
    name varchar(150) not null unique,
    muscular_group varchar(40) not null
);

CREATE TABLE tb_workout(
    workout_id bigserial not null primary key,
    name varchar(150) not null,
    user_id bigint not null,
    exercise_id bigint not null,
    constraint fk_user_id foreign key(user_id) references tb_user(user_id),
    constraint fk_exercise_id foreign key (exercise_id) references tb_exercise(exercise_id)
);