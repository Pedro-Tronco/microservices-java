create table tb_inventory (
	user_id int not null,
	product_id int not null,
	is_favorite boolean not null default(false),
	last_access TIMESTAMPTZ,
	bookmarks varchar(50) default '',
	primary key (user_id, product_id)
);