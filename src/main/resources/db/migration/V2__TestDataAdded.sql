INSERT INTO new.users (is_moderator, reg_time, name, email, password)
VALUES
('true',1579956771856,'Sergey', 'wert@dfre.cf', '454545');
---------------
INSERT INTO new.posts
(is_active, moderation_status, moderator_id, user_id,
time, title, text, view_count )
VALUES ('true', 'NEW', 1, 1, 1579956779856, 'Это пост номер 1 для теста',
'А это текс поста номер один для теста', 6);

INSERT INTO new.posts
(is_active, moderation_status, moderator_id, user_id,
time, title, text, view_count )
VALUES ('true', 'NEW', 1, 1, 1579956779856, 'Это пост номер 2 для теста',
'А это текс поста номер ДВА для теста', 3);
----------------
Insert Into new.tags (name) VALUES ('Tag_1');
Insert Into new.tags (name) VALUES ('Tag_2');
Insert Into new.tags (name) VALUES ('Tag_3');
Insert Into new.tags (name) VALUES ('Tag_4');
Insert Into new.tags (name) VALUES ('Tag_5');
------------------------
Insert Into new.tag2post (post_id, tag_id) VALUES (1,1);
Insert Into new.tag2post (post_id, tag_id) VALUES (1,2);
Insert Into new.tag2post (post_id, tag_id) VALUES (2,3);
Insert Into new.tag2post (post_id, tag_id) VALUES (2,4);
-----------------
Insert Into new.global_settings
(code, name, value)
VALUES
('MULTIUSER_MODE', 'Многопользовательский режим', 'true'),
('POST_PREMODERATION', 'Премодерация постов', 'false'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'true');