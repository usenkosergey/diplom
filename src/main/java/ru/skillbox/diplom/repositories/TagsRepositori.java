package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepositori extends JpaRepository<Tag, Integer> {

    @Query("SELECT t.text, count(*) AS count_tag " +
            "FROM Tag2post AS tp " +
            "JOIN Post AS p ON p.id = tp.postId " +
            "JOIN Tag AS t ON tp.tagId = t.id " +
            "WHERE p.isActive = true " +
            "AND p.eModerationStatus = 'ACCEPTED' " +
            "AND p.time <= :currentTime " +
            "GROUP BY t.text " +
            "ORDER BY count_tag DESC ")
    List<Object[]> tagsForTopic(long currentTime);

    Optional<Tag> findByText(String name);
}
