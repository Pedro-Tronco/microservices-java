create table tb_cart (
	user_id bigint,
	product_id bigint,
	is_selected boolean default(true),
	inclusion TIMESTAMPTZ,
	primary key (user_id, product_id)
);