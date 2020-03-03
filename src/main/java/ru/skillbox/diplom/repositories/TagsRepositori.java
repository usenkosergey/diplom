package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepositori extends JpaRepository<Tag, Integer> {

    @Query(nativeQuery = true,
            value = "INSERT INTO tags (name) values (:tagNew) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id;")
    Integer addNewTag(@Param("tagNew") String tag);

    @Query(nativeQuery = true,
    value = "select tags.name, count(*) from tag2post\n" +
            "JOIN posts on posts.id = tag2post.post_id\n" +
            "JOIN tags on tag2post.tag_id = tags.id\n" +
            "WHERE moderation_status = 'ACCEPTED' and is_active = true\n" +
            "and time <= (:currentTime) GROUP BY tags.name;")
    List<Object[]> tagsForTopic(@Param("currentTime") long currentTime);

    /*
    select new.tags.name, count(*)
From new.tag2post JOIN new.tags
on new.tags.id = new.tag2post.tag_id GROUP BY new.tags.name;
     */
/* //TODO выбор нужных тегов
select new.tags.name, count(*) from new.tag2post
JOIN new.posts on new.posts.id=new.tag2post.post_id
JOIN new.tags on new.tag2post.tag_id = new.tags.id
WHERE moderation_status = 'ACCEPTED' and is_active = true
and time <= 1580392831678 GROUP BY new.tags.name //TODO текущее время

 */
    Optional<Tag> findByText(String text);
}
