create table tb_user (
        id serial not null,
        email varchar(255) not null,
        name varchar(255) not null,
        password varchar(255) not null,
        type smallint check (type between 0 and 2),
        prefered_currency varchar(5) not null,
        primary key (id)
    )