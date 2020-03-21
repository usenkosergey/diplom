CREATE TABLE users
(
id SERIAL PRIMARY KEY,
is_moderator boolean NOT NULL,
reg_time BIGINT NOT NULL,
name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
code VARCHAR(255),
photo TEXT
);

CREATE TABLE posts
(
id SERIAL PRIMARY KEY,
is_active boolean NOT NULL,
moderation_status VARCHAR(8) NOT NULL,
moderator_id INT,
user_id INT NOT NULL REFERENCES users (id),
time BIGINT NOT NULL,
title VARCHAR(255) NOT NULL,
text TEXT NOT NULL,
view_count INT NOT NULL
);

CREATE TABLE post_votes
(
id SERIAL PRIMARY KEY,
user_id INT NOT NULL,
post_id INT NOT NULL,
time BIGINT NOT NULL,
value INT NOT NULL
);
CREATE INDEX value ON post_votes (value);
CREATE INDEX user_id ON post_votes (user_id);
CREATE INDEX user_post_id ON post_votes (user_id, post_id);

CREATE TABLE tags
(
id SERIAL PRIMARY KEY,
name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE tag2post
(
id SERIAL PRIMARY KEY,
post_id INT NOT NULL REFERENCES posts (id),
tag_id INT NOT NULL REFERENCES tags (id)
);

CREATE TABLE post_comments
(
id SERIAL PRIMARY KEY,
parent_id INT,
post_id INT NOT NULL,
user_id INT NOT NULL REFERENCES users (id),
text TEXT NOT NULL,
time BIGINT NOT NULL
);
CREATE INDEX post_id ON post_comments (post_id);

CREATE TABLE captcha_codes
(
id SERIAL PRIMARY KEY,
time BIGINT NOT NULL,
code TEXT UNIQUE NOT NULL,
secret_code TEXT NOT NULL
);

CREATE TABLE global_settings
(
id SERIAL PRIMARY KEY,
code VARCHAR(255) NOT NULL,
name VARCHAR(255) NOT NULL,
value boolean NOT NULL
);

Insert Into global_settings
(code, name, value)
VALUES
('MULTIUSER_MODE', 'Многопользовательский режим', 'true'),
('POST_PREMODERATION', 'Премодерация постов', 'false'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'true');