
create table if not exists phone_no_otp(
    id serial primary key,
    phone varchar(10) unique not null,
    otp varchar(6) not null,
    gen_ts timestamptz not null,
    count int
);

create table if not exists users(
    id serial primary key,
    is_active boolean NOT NULL,
    role varchar NOT NULL,
    created_at timestamptz,
    updated_at timestamptz,
    deleted_at timestamptz,
    email varchar,
    name varchar,
    phone varchar NOT NULL,
    display_photo_url varchar
    -- foreign key(otp_phone) references user_phone_no_otp(otp_phone) on delete cascade on update cascade

);


create table if not exists user_address(
    id serial primary key,
    users_id int references users on delete cascade on update cascade,
    address varchar NOT NULL,
    display_name varchar default 'HOME' NOT NULL,
    is_default boolean default false,
    created_at timestamptz not null,
    updated_at timestamptz,
    deleted_at timestamptz,
    foreign key(users_id) references users(id)
);

create table if not exists user_refresh_token(
    id serial primary key,
    users_id int references users on delete cascade on update cascade,
    token varchar NOT NULL,
    created_at timestamptz not null,
    expire_ts timestamptz,
    foreign key(users_id) references users(id)
);

create table if not exists merchant(
    id serial primary key,
    merchant_name varchar not null,
    contact_person_name varchar not null,
    image_url varchar,
    speciality varchar not null,
    location varchar not null,
    created_at timestamptz not null,
    updated_at timestamptz ,
    rank int not null,
    is_deleted boolean default false
);

create table if not exists product_category
(
	id serial
		constraint product_category_pkey
			primary key,
	name varchar not null,
	description varchar not null,
	image_url varchar,
	show_on_home_screen boolean,
	created_at timestamp with time zone not null,
	updated_at timestamp with time zone,
	deleted_at timestamp with time zone,
	is_deleted boolean default false,
	lft integer not null,
	rgt integer not null,
	rank integer not null,
	parent_id integer,
	constraint product_category_parent_id_rank_key
		unique (parent_id, rank),
	constraint valid_ptr_check
		check (lft < rgt)
);

create table if not exists product(
	id serial
		constraint product_pkey
			primary key,
	category_id integer
		constraint product_category_id_fkey
			references product_category
				on update cascade on delete cascade,
	name varchar not null,
	created_at timestamp with time zone not null,
	updated_at timestamp with time zone,
	image_url varchar,
	show_on_home_screen boolean,
	description varchar,
	rank integer not null,
	is_deleted boolean default false,
	merchant_id integer
		constraint product_merchant_id_fkey
			references merchant
				on update cascade on delete cascade,
	is_stock_available boolean default false not null
);

create table if not exists merchant_category_map(
    id serial primary key,
    merchant_id int references merchant on delete cascade on update cascade,
    category_id int references product_category on delete cascade on update cascade,
    created_at timestamptz not null,
    updated_at timestamptz ,
    rank int not null,
    is_deleted boolean default false,
    foreign key(category_id) references product_category(id),
    foreign key(merchant_id) references merchant(id)
);

create table if not exists product_variant(
    id serial primary key,
    sku_id varchar not null,
    product_id int not null references product on delete cascade on update cascade,
    description varchar,
    created_at timestamptz not null,
    updated_at timestamptz,
    deleted_at timestamptz,
    features jsonb,
    discount_percentage int,
    amount float not null,
    is_default boolean,
    stock_available int not null,
    foreign key(product_id) references product(id)
);


create table if not exists orders (
    id serial primary key,
    user_id int not null,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    total_amount float not null,
    total_discount_amount int,
    status varchar,
    events varchar,
    phone varchar,
    address varchar,
    locality varchar,
    invoice_url varchar
);

create table if not exists item(
    id serial primary key,
    order_id int not null references orders on delete cascade on update cascade,
    product_id int not null,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    deleted_at timestamptz,
    quantity int not null,
    amount float not null,
    discount_amount float default 0.0,
    product_name varchar not null,
    product_image_url varchar,
    product_attributes jsonb,
    foreign key(order_id) references orders(id)
);

create table if not exists cart(
    id serial primary key,
    user_id int not null  references users on delete no action on update no action,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    product_variant_id int not null references product_variant on delete cascade on update cascade,
    quantity int not null,
    unique(user_id, product_variant_id),
    foreign key(user_id) references users(id),
    foreign key(product_variant_id) references product_variant(id)
);

create table if not exists customer_feedback(
    id serial primary key,
    phone varchar NOT NULL,
    name varchar,
    feedback varchar NOT NULL,
    action_taken varchar,
    created_at timestamptz NOT NULL,
    updated_at timestamptz
);


create table if not exists app_version(
    id serial primary key,
    version varchar NOT NULL,
    is_force_update boolean NOT NULL,
    is_active boolean NOT NULL,
    created_at timestamptz NOT NULL
);


create table if not exists task(
    id serial primary key,
    order_id int not null,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    deleted_at timestamptz,
    total_item_count int not null,
    total_collection_amount int not null,
    invoice_image_url varchar,
    events jsonb,
    customer_details jsonb
);


-- PROCEDURES

--CREATE OR REPLACE PROCEDURE insertNode(
--    parentId INTEGER,
--    catName VARCHAR,
--    catRank INTEGER,
--    catDesc VARCHAR,
--    catShowOnHomeScreen BOOLEAN,
--    catImageUrl VARCHAR
--) AS
--$$
--DECLARE
--    myRight    INTEGER;
--    lastIDUsed INTEGER;
--BEGIN
--    LOCK TABLE product_category;
--    SELECT rgt INTO myRight FROM product_category WHERE id = parentId;
--    SELECT MAX(id) into lastIDUsed FROM product_category;
--    UPDATE product_category SET rgt = rgt + 2 WHERE rgt >= myRight;
--    UPDATE product_category SET lft = lft + 2 WHERE lft > myRight;
--    INSERT INTO product_category(id, name, lft, rgt, description, created_at, rank, show_on_home_screen, image_url, parent_id)
--    VALUES (lastIDUsed + 1, catName, myRight, myRight + 1, catDesc, now(), catRank, catShowOnHomeScreen, catImageUrl, parentId);
--END;
--$$ LANGUAGE plpgsql;

--create procedure check_and_make_category_and_merchant_category_mapping_active(categoryid integer, merchantid integer)
--    language plpgsql
--as
--$$
--declare
--    latestRank       integer;
--    lastIdUsed       integer;
--    currIsDeletedVal bool;
--    newIsDeletedVal  bool;
--begin
--    -- looping through all the parents of given category
--    loop
--        -- exit when reached root
--        exit when categoryId = 1;
--        -- store current is_deleted value of current(child) product category
--        select is_deleted into currIsDeletedVal from product_category where id = categoryId;
--        -- for child categories check if at least 1 active product is present then,
--        -- for parent categories check if active child present then,
--        -- make product_category record active
--        if exists(select * from product where category_id = categoryId and is_deleted = false) or
--           exists(select * from product_category where parent_id = categoryId and is_deleted = false)
--        then
--            update product_category
--            set is_deleted = false,
--                updated_at= now(),
--                deleted_at = null
--            where id = categoryId
--              and is_deleted = true;
--        end if;
--
--        -- check if merchant_category_mapping present
--        if exists(select * from merchant_category_map where category_id = categoryId and merchant_id = merchantId)
--        then
--            -- make merchant_category_map record active
--            update merchant_category_map
--            set is_deleted = false,
--                updated_at= now()
--            where category_id = categoryId
--              and merchant_id = merchantId
--              and is_deleted = true;
--        else
--            if exists(select * from merchant_category_map) then
--               select max(id) into lastIdUsed from merchant_category_map;
--            else
--               lastIdUsed = 0;
--            end if;
--            if exists(select * from merchant_category_map where merchant_id = merchantId and rank is not null) then
--                select max(rank) into latestRank from merchant_category_map where merchant_id = merchantId;
--            else
--                latestRank = 0;
--            end if;
--            insert into merchant_category_map(id, merchant_id, category_id, created_at, updated_at, rank, is_deleted)
--            values (lastIdUsed + 1, merchantId, categoryId, now(), null, latestRank + 1, false);
--        end if;
--        -- if there is no change in is_deleted value and merchant_category mapping exists for parent categories
--        -- then come out of loop
--        select is_deleted into newIsDeletedVal from product_category where id = categoryId;
--        -- fetching parentId for next loop iteration
--        select parent_id into categoryId from product_category where id = categoryId;
--        if currIsDeletedVal = newIsDeletedVal and
--           exists(select * from merchant_category_map where category_id = categoryId and merchant_id = merchantId)
--        then
--            -- exit by failing loop condition
--            categoryId = 1;
--        end if;
--    end loop;
--    return;
--end ;
--$$;

--create procedure check_and_make_category_and_merchant_category_mapping_inactive(categoryid integer, merchantid integer)
--   language plpgsql
--as
--$$
--declare
--   currIsDeletedVal bool;
--   newIsDeletedVal  bool;
--begin
--   -- looping through all the parents of given category
--   loop
--       -- exit when reached root
--       exit when categoryId = 1;
--       -- store current is_deleted value of current(child) product category
--       select is_deleted into currIsDeletedVal from product_category where id = categoryId;
--       -- for this merchant,
--       -- for child product category if there is no active product present
--       -- for parent product category if there is no active child product category present
--       -- mark merchant_category_map record inactive
--       if not exists(select *
--                     from product
--                     where category_id = categoryId
--                       and merchant_id = merchantId
--                       and is_deleted = false) and
--          not exists(select * from product_category where parent_id = categoryId and is_deleted = false)
--       then
--           update merchant_category_map
--           set is_deleted = true,
--               updated_at = now()
--           where merchant_id = merchantId
--             and category_id = categoryId
--             and is_deleted = false;
--       end if;
--       -- if there is no active mapping present for this category in merchant_category_map
--       -- mark product_category record inactive
--       if not exists(
--               select *
--               from merchant_category_map
--               where category_id = categoryId
--                 and is_deleted = false
--           ) then
--           update product_category
--           set is_deleted = true,
--               updated_at = now(),
--               deleted_at = now()
--           where id = categoryId
--             and is_deleted = false;
--       end if;
--
--       -- if there is change in is_deleted value then only propagate this change to parent categories
--       select is_deleted into newIsDeletedVal from product_category where id = categoryId;
--       if currIsDeletedVal != newIsDeletedVal then
--           -- fetching parentId for next loop iteration
--           select parent_id into categoryId from product_category where id = categoryId;
--       else
--           -- exit by failing loop condition
--           categoryId = 1;
--       end if;
--   end loop;
--   return;
--end;
--$$;

