create table tb_roles(
    id bigserial not null primary key,
    name varchar(150) not null unique
);