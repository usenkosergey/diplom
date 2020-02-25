CREATE TABLE new.users
(
id SERIAL PRIMARY KEY,
is_moderator boolean NOT NULL,
--reg_time TIMESTAMP NOT NULL,
reg_time BIGINT NOT NULL,
name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
code VARCHAR(255),
photo TEXT
);

CREATE TABLE new.posts
(
id SERIAL PRIMARY KEY,
is_active boolean NOT NULL,
moderation_status VARCHAR(8) NOT NULL,
moderator_id INT,
user_id INT NOT NULL REFERENCES users (id),
--time TIMESTAMP NOT NULL,
time BIGINT NOT NULL,
title VARCHAR(255) NOT NULL,
text TEXT NOT NULL,
view_count INT NOT NULL
);

CREATE TABLE new.post_votes
(
id SERIAL PRIMARY KEY,
user_id INT NOT NULL,
--user_id INT NOT NULL REFERENCES users (id),
post_id INT NOT NULL,
--post_id INT NOT NULL REFERENCES posts (id),
time BIGINT NOT NULL,
--time TIMESTAMP NOT NULL,
value SMALLINT NOT NULL
);

CREATE TABLE new.tags
(
id SERIAL PRIMARY KEY,
name VARCHAR(255) UNIQUE NOT NULL
--name VARCHAR(255) NOT NULL
);

CREATE TABLE new.tag2post
(
id SERIAL PRIMARY KEY,
post_id INT NOT NULL REFERENCES posts (id),
tag_id INT NOT NULL REFERENCES tags (id)
);

CREATE TABLE new.post_comments
(
id SERIAL PRIMARY KEY,
parent_id INT,
post_id INT NOT NULL,
--post_id INT NOT NULL REFERENCES posts (id),
user_id INT NOT NULL REFERENCES users (id),
text TEXT NOT NULL,
--time TIMESTAMP NOT NULL
time BIGINT NOT NULL
);

CREATE TABLE new.captcha_codes
(
id SERIAL PRIMARY KEY,
--time TIMESTAMP NOT NULL,
time BIGINT NOT NULL,
code TEXT NOT NULL,
secret_code TEXT NOT NULL
);

CREATE TABLE new.global_settings
(
id SERIAL PRIMARY KEY,
code VARCHAR(255) NOT NULL,
name VARCHAR(255) NOT NULL,
value VARCHAR(255) NOT NULL
);