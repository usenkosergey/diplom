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
            value = "SELECT tags.name, count(*) " +
                    "FROM tag2post " +
                    "JOIN posts ON posts.id = tag2post.post_id " +
                    "JOIN tags ON tag2post.tag_id = tags.id " +
                    "WHERE moderation_status = 'ACCEPTED' " +
                    "AND is_active = true " +
                    "AND time <= (:currentTime) " +
                    "GROUP BY tags.name " +
                    "ORDER BY count DESC;")
    List<Object[]> tagsForTopic(@Param("currentTime") long currentTime);

    Optional<Tag> findByText(String name);
}
