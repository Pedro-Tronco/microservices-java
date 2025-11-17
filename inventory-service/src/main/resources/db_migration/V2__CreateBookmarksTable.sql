create table tb_bookmarks (
	user_id int not null,
	bookmark_id uuid,
	description varchar(50) not null,
	hex_color char(7) not null,
	primary key (user_id, bookmark_id)
);