package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Comment;
import ru.skillbox.diplom.entities.Post;

import java.util.List;

@Repository
public interface PostRepositori extends PagingAndSortingRepository<Post, Integer> {
//TODO экранирование спец символов проверить и сделать
    @Query(nativeQuery = true,
    value = "SELECT COUNT(*) FROM posts WHERE moderation_status = 'ACCEPTED' and is_active = true " +
            "and time <= (:currentTime);")
    Integer countActualPosts(@Param("currentTime") long currentTime);

    @Query(nativeQuery = true,
            value = "SELECT * FROM posts WHERE moderation_status = 'ACCEPTED' and is_active = true " +
                    "and time <= (:currentTime) ORDER BY time ASC LIMIT 10;")
    List<Post> getListActualPosts(@Param("currentTime") long currentTime);

}
