package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepositori extends PagingAndSortingRepository<Post, Integer> {
    //TODO экранирование спец символов проверить и сделать
    //1583250619160 время для теста
    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM posts WHERE moderation_status = 'ACCEPTED' and is_active = true " +
                    "and time <= (:currentTime);")
    Integer countActualPosts(@Param("currentTime") long currentTime);

    @Query(nativeQuery = true,
            value = "SELECT * FROM posts WHERE moderation_status = 'ACCEPTED' and is_active = true " +
                    "and time <= (:currentTime) ORDER BY time DESC LIMIT 10 OFFSET (:offset);")
    List<Post> getListRecentPosts(@Param("currentTime") long currentTime, @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM posts WHERE moderation_status = 'ACCEPTED' and is_active = true " +
                    "and time <= (:currentTime) ORDER BY time ASC LIMIT 10 OFFSET (:offset);")
    List<Post> getListEarlyPosts(@Param("currentTime") long currentTime, @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "SELECT posts.*, count(post_votes.post_id) FROM posts left JOIN " +
                    "post_votes ON posts.id = post_votes.post_id where " +
                    "value = 1 or value isnull " +
                    "and moderation_status = 'ACCEPTED' " +
                    "and is_active = true and posts.time <= (:currentTime) " +
                    "group by post_votes.post_id, posts.id ORDER BY count DESC LIMIT 10 OFFSET (:offset);")
    List<Post> getListBestPosts(@Param("currentTime") long currentTime, @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "select posts.*, count(post_comments.post_id) " +
                    "FROM posts left JOIN post_comments " +
                    "ON posts.id = post_comments.post_id " +
                    "where moderation_status = 'ACCEPTED' " +
                    "and is_active = true and posts.time <= (:currentTime) " +
                    "group by post_comments.post_id, posts.id " +
                    "ORDER BY count DESC LIMIT 10 OFFSET (:offset);")
    List<Post> getListCommentPosts(@Param("currentTime") long currentTime, @Param("offset") int offset);


    @Query(nativeQuery = true,
            value = "select count(value) from post_votes where value = (:value) and post_id = (:id) group by value;")
    Optional<Integer> countLike(@Param("id") int id, @Param("value") int value);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE posts SET view_count = view_count + 1 WHERE id = (:id);")
    Integer updateViewCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "SELECT SUM(view_count) FROM posts;")
    long sumByViewCount();

    @Query(nativeQuery = true,
            value = "SELECT * FROM posts ORDER BY id ASC LIMIT 1;")
    Optional<Post> firstPublication();

    @Query(nativeQuery = true,
            value = "SELECT posts.* FROM tags " +
                    "JOIN tag2post ON tags.id = tag2post.tag_id " +
                    "JOIN posts ON posts.id=tag2post.post_id WHERE name = (:tag) " +
                    "AND moderation_status = 'ACCEPTED' AND is_active = true " +
                    "AND posts.time <= (:currentTime) " +
                    "ORDER BY id DESC LIMIT 10 OFFSET (:offset);")
    List<Post> allPostsByTag(@Param("tag") String tag,
                                 @Param("currentTime") long currentTime,
                                 @Param("offset") int offset);
}
