CREATE EXTENSION unaccent;

create table tb_product (
	id serial not null,
	user_id bigint not null,
	title varchar(255) not null,
	author varchar(255) not null,
	synopsis varchar(1024),
	language varchar(5) not null,
	publisher varchar(100) not null,
	file_extension varchar(20) not null,
	genre_tags varchar(255),
	page_count int not null,
	download_url varchar(512),
	image_url varchar(255),
	currency varchar(3) not null,
	price float(53) not null,
	primary key (id)
);