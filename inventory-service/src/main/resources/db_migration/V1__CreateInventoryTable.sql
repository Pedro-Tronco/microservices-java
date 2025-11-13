create table tb_inventory (
	user_id int not null,
	product_id int not null,
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
	is_favorite boolean not null default(false),
	bookmarks varchar(50),
	primary key (user_id, product_id)
);