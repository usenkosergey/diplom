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
            value = "select tags.name, count(*) from tag2post\n" +
                    "JOIN posts on posts.id = tag2post.post_id\n" +
                    "JOIN tags on tag2post.tag_id = tags.id\n" +
                    "WHERE moderation_status = 'ACCEPTED' and is_active = true\n" +
                    "and time <= (:currentTime) GROUP BY tags.name ORDER BY count DESC LIMIT 20;")
    List<Object[]> tagsForTopic(@Param("currentTime") long currentTime);

    Optional<Tag> findByText(String name);
}
