create table tb_review (
	product_id bigint,
	user_id bigint,
	grade integer CHECK (grade >= 1 AND grade <= 5) not null,
	title varchar(100),
	comment varchar(1000),
	post_date TIMESTAMPTZ, 
	primary key (product_id, user_id)
)