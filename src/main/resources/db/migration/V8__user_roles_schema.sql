create table tb_user_roles(
    user_id bigint not null,
    role_id bigint not null,
    primary key(user_id, role_id),
    foreign key (user_id) references tb_user on delete cascade,
    foreign key(role_id) references tb_roles on delete cascade
);