CREATE TABLE new.users
(
id SERIAL PRIMARY KEY,
is_moderator boolean NOT NULL DEFAULT false,
reg_time TIMESTAMP NOT NULL,
name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
code VARCHAR(255),
photo TEXT
);

CREATE TYPE e_moderation_status AS ENUM ('NEW', 'ACCEPTED', 'DECLINED');

CREATE TABLE new.posts
(
id SERIAL PRIMARY KEY,
is_active boolean NOT NULL DEFAULT true,
moderation_status e_moderation_status NOT NULL DEFAULT 'NEW',
moderator_id INT,
user_id INT NOT NULL REFERENCES users (id),
time TIMESTAMP NOT NULL,
title VARCHAR(255) NOT NULL,
text TEXT NOT NULL,
view_count INT NOT NULL
);

CREATE TABLE new.post_votes
(
id SERIAL PRIMARY KEY,
user_id INT NOT NULL REFERENCES users (id),
post_id INT NOT NULL REFERENCES posts (id),
time TIMESTAMP NOT NULL,
value SMALLINT NOT NULL
);

CREATE TABLE new.tags
(
id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL
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
post_id INT NOT NULL REFERENCES posts (id),
user_id INT NOT NULL REFERENCES users (id),
time TIMESTAMP NOT NULL
);

CREATE TABLE new.captcha_codes
(
id SERIAL PRIMARY KEY,
time TIMESTAMP NOT NULL,
code SMALLINT NOT NULL,
secret_code SMALLINT NOT NULL
);

CREATE TABLE new.global_settings
(
id SERIAL PRIMARY KEY,
code VARCHAR(255) NOT NULL,
name VARCHAR(255) NOT NULL,
value VARCHAR(255) NOT NULL
);