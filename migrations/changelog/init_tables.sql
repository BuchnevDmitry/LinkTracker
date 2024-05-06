--liquibase formatted sql;

--changeset DmitryBuchnev:1
--comment create table сhat
create table chat
(
    id bigint not null,
    created_at timestamp with time zone not null,
    created_by text not null,
    primary key (id)
);
--rollback drop table chat

--changeset DmitryBuchnev:2
--comment create table link
create table link
(
    id bigint generated always as identity,
    url text not null,
    last_check_time timestamp with time zone not null,
    created_at timestamp with time zone not null,
    created_by text not null,
    primary key (id),
    unique (url)
);
--rollback drop table link

--changeset DmitryBuchnev:3
--comment create table chat_link
create table chat_link
(
    chat_id bigint not null,
    link_id bigint not null,
    foreign key (chat_id) references chat (id),
    foreign key (link_id) references link (id),
    unique (chat_id, link_id)
);
--rollback drop table chat_link

