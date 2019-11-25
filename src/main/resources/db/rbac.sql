create table if not exists permission
(
    id bigint auto_increment,
    parent_id bigint null comment '父权限',
    name varchar(64) not null comment '名称',
    code varchar(64) not null comment 'code',
    uri varchar(255) not null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null,
    constraint permission_id_uindex
        unique (id)
)
    comment '权限';

alter table permission
    add primary key (id);

create table if not exists role
(
    id bigint auto_increment,
    parent_id bigint null comment '父角色',
    name varchar(64) not null comment '名称',
    code varchar(64) not null comment 'code',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null,
    constraint role_id_uindex
        unique (id)
)
    comment '角色';

alter table role
    add primary key (id);

create table if not exists role_permission
(
    id bigint auto_increment,
    role_id bigint not null comment '角色 id',
    permission_id bigint not null comment '权限id',
    constraint role_permission_id_uindex
        unique (id)
)
    comment '角色权限';

alter table role_permission
    add primary key (id);

create table if not exists user
(
    id bigint auto_increment,
    username varchar(64) not null comment '用户名',
    password varchar(64) not null comment '密码',
    phone varchar(64) null comment '手机',
    email varchar(64) null comment '邮箱',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null,
    constraint user_id_uindex
        unique (id)
)
    comment '用户';

alter table user
    add primary key (id);

create table if not exists user_role
(
    id bigint auto_increment,
    user_id bigint not null,
    role_id bigint not null,
    constraint user_role_id_uindex
        unique (id)
)
    comment '用户角色';

alter table user_role
    add primary key (id);



insert into `permission`(`id`, `parent_id`, `name`, `code`, `uri`)
values (1, 0, '系统管理', 'system', '/'),
       (2, 1, '用户管理', 'system:user', '/user'),
       (3, 2, '查看用户', 'system:user:select', '/user/select'),
       (4, 2, '新增用户', 'system:user:create', '/user/create'),
       (5, 2, '编辑用户', 'system:user:update', '/user/update'),
       (6, 2, '删除用户', 'system:user:delete', '/user/delete');


insert into `role`(`id`, `parent_id`, `name`, `code`)
values (1, 0, '超级管理员', 'admin');


insert into `role_permission`(`id`, `role_id`, `permission_id`)
values (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 1, 4),
       (5, 1, 5),
       (6, 1, 6);


insert into `user`(`id`, `username`, `password`, `phone`, `email`)
values (1, 'admin', '$2a$10$UfmnQugjbw/H4C.FExEyNeRZNhJikfGJ5zD3Yew28xM0b1c04XClS', '18888888888', 'admin@mfhcd.com');


insert into `user_role`(`id`, `user_id`, `role_id`)
values (1, 1, 1);


select p.*
from user u
         left join user_role as ur on u.id = ur.user_id
         left join role as r on ur.role_id = r.id
         left join role_permission as rp on r.id = rp.role_id
         left join permission as p on rp.permission_id = p.id
where u.id = 1;
