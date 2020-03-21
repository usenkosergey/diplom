package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.Post;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PostRepositori extends PagingAndSortingRepository<Post, Integer> {
    //TODO экранирование спец символов проверить и сделать
    //1583250619160 время для теста

    String is_active = "is_active = true ";
    String moderation_status = "moderation_status = 'ACCEPTED'";
    String current_timestamp = "ROUND(EXTRACT(epoch FROM current_timestamp)*1000)";
    String limit = "LIMIT 10 OFFSET (:offset)";

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) " +
                    "FROM posts " +
                    "WHERE " + moderation_status +
                    "AND " + is_active +
                    "AND time <= " + current_timestamp + " ;")
    Integer countActualPosts();


    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE " + moderation_status +
                    "AND " + is_active +
                    "AND time <= " + current_timestamp +
                    "ORDER BY time DESC " +
                    limit + " ;")
    List<Post> getListRecentPosts(@Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE moderation_status = 'ACCEPTED' " +
                    "and is_active = true " +
                    "and time >= (:startTime) " +
                    "and time <= (:endTime)" +
                    "ORDER BY time DESC " +
                    "LIMIT 10 OFFSET (:offset);")
    List<Post> getPostByDate(@Param("startTime") long startTime,
                             @Param("endTime") long engTime,
                             @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "SELECT count(*) " +
                    "FROM posts " +
                    "WHERE moderation_status = 'ACCEPTED' " +
                    "and is_active = true " +
                    "and time >= (:startTime) " +
                    "and time <= (:endTime);")
    Integer getCountPostByDate(@Param("startTime") long startTime,
                               @Param("endTime") long engTime);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE moderation_status = 'ACCEPTED' " +
                    "and is_active = true " +
                    "and time <= (:currentTime) " +
                    "ORDER BY time ASC " +
                    "LIMIT 10 OFFSET (:offset);")
    List<Post> getListEarlyPosts(@Param("currentTime") long currentTime,
                                 @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "SELECT posts.*, count(post_votes.post_id) " +
                    "FROM posts " +
                    "left JOIN post_votes " +
                    "ON posts.id = post_votes.post_id " +
                    "where value = 1 or value isnull " +
                    "and moderation_status = 'ACCEPTED' " +
                    "and is_active = true " +
                    "and posts.time <= (:currentTime) " +
                    "group by post_votes.post_id, posts.id " +
                    "ORDER BY count DESC " +
                    "LIMIT 10 OFFSET (:offset);")
    List<Post> getListBestPosts(@Param("currentTime") long currentTime,
                                @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "select posts.*, count(post_comments.post_id) " +
                    "FROM posts " +
                    "left JOIN post_comments " +
                    "ON posts.id = post_comments.post_id " +
                    "where moderation_status = 'ACCEPTED' " +
                    "and is_active = true " +
                    "and posts.time <= (:currentTime) " +
                    "group by post_comments.post_id, posts.id " +
                    "ORDER BY count DESC " +
                    "LIMIT 10 OFFSET (:offset);")
    List<Post> getListCommentPosts(@Param("currentTime") long currentTime,
                                   @Param("offset") int offset);


    @Query(nativeQuery = true,
            value = "select count(value) " +
                    "from post_votes " +
                    "where value = (:value) " +
                    "and post_id = (:id) group by value;")
    Optional<Integer> countLike(@Param("id") int id,
                                @Param("value") int value);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE posts " +
                    "SET view_count = view_count + 1 " +
                    "WHERE id = (:id);")
    Integer updateViewCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "SELECT SUM(view_count) " +
                    "FROM posts;")
    long sumByViewCount();

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "ORDER BY id ASC " +
                    "LIMIT 1;")
    Optional<Post> firstPublication();

    @Query(nativeQuery = true,
            value = "SELECT posts.* " +
                    "FROM tags " +
                    "JOIN tag2post ON tags.id = tag2post.tag_id " +
                    "JOIN posts ON posts.id=tag2post.post_id " +
                    "WHERE name = (:tag) " +
                    "AND moderation_status = 'ACCEPTED' " +
                    "AND is_active = true " +
                    "AND posts.time <= (:currentTime) " +
                    "ORDER BY id DESC " +
                    "LIMIT 10 OFFSET (:offset);")
    List<Post> allPostsByTag(@Param("tag") String tag,
                             @Param("currentTime") long currentTime,
                             @Param("offset") int offset);

    @Query(nativeQuery = true,
            value = "select to_char(date_trunc('year', to_timestamp(div(time,1000))), 'YYYY') " +
                    "from posts " +
                    "WHERE is_active = true " +
                    "AND moderation_status = 'ACCEPTED' " +
                    "AND posts.time <= (:currentTime) " +
                    "group by date_trunc('year', to_timestamp(div(time,1000)))" +
                    "ORDER BY to_char DESC;")
    List<String> getYearToCalendar(@Param("currentTime") long currentTime);

    @Query(nativeQuery = true,
            value = "select to_char(date_trunc('day', to_timestamp(div(time,1000))), 'YYYY-MM-DD'), count(*) " +
                    "from posts " +
                    "WHERE is_active = true " +
                    "and moderation_status = 'ACCEPTED' " +
                    "AND posts.time <= (:currentTime) " +
                    "and Extract(year FROM  date_trunc('day', to_timestamp(div(time,1000)))) = (:year) " +
                    "group by date_trunc('day', to_timestamp(div(time,1000))) " +
                    "ORDER BY to_char ASC;")
    List<Object[]> listPostForYears(@Param("currentTime") long currentTime,
                                    @Param("year") int year);
//    TODO не работает ссылка с фронта для поиска
//    @Query(nativeQuery = true,
//            value = "SELECT COUNT(*) " +
//                    "FROM posts " +
//                    "WHERE moderation_status = 'ACCEPTED' " +
//                    "and is_active = true " +
//                    "and time <= (:currentTime)" +
//                    "and text like '%(:searchString)%';")
//    Integer countPostsBySearch(@Param("currentTime") long currentTime,
//                               @Param("searchString") String searchString);
//
//    @Query(nativeQuery = true,
//            value = "SELECT * FROM posts " +
//                    "WHERE moderation_status = 'ACCEPTED' " +
//                    "and is_active = true " +
//                    "and text like '%(:searchString)%' " +
//                    "and time <= (:currentTime) " +
//                    "ORDER BY time DESC LIMIT 10 OFFSET (:offset);")
//    List<Post> getPostBySearch(@Param("currentTime") long currentTime,
//                               @Param("searchString") String searchString);
//TODO не работает ссылка с фронта для поиска

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE user_id = (:userId) " +
                    "AND is_active = false " +
                    limit + ";")
    List<Post> getMyPostsInactive(@Param("offset") int offset,
                                  @Param("userId") int userId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) " +
                    "FROM posts " +
                    "WHERE user_id = (:userId) " +
                    "AND is_active = false " +
                    limit + ";")
    Optional<Integer> countMyPostsInactive(@Param("offset") int offset,
                                           @Param("userId") int userId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) " +
                    "FROM posts " +
                    "WHERE user_id = (:userId) " +
                    "AND " + is_active +
                    "AND moderation_status = (:status) " +
                    limit + ";")
    Optional<Integer> countMyPostsActive(@Param("offset") int offset,
                                         @Param("userId") int userId,
                                         @Param("status") String status);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE user_id = (:userId) " +
                    "AND " + is_active +
                    "AND moderation_status = (:status) " +
                    limit + ";")
    List<Post> getMyPostsActive(@Param("offset") int offset,
                                @Param("userId") int userId,
                                @Param("status") String status);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM posts WHERE user_id = (:userId);")
    Optional<Integer> countByUser(@Param("userId") int userId);

    @Query(nativeQuery = true,
            value = "SELECT SUM(view_count) FROM posts WHERE user_id = (:user_id);")
    Optional<Integer> sumMyViewCount(@Param("user_id") int user_id);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts WHERE user_id = (:userId)" +
                    "ORDER BY id ASC " +
                    "LIMIT 1;")
    Optional<Post> firstMyPublication(@Param("userId") int userId);

    int countByeModerationStatusAndIsActive(EModerationStatus eModerationStatus, Boolean status);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*)" +
                    "FROM posts " +
                    "WHERE moderation_status = 'NEW' " +
                    "AND is_active = true ;")
    Optional<Long> countNewPosts();

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE moderation_status = 'NEW'" +
                    "AND " + is_active + limit + ";")
    List<Post> getNewPostsForModeration(@Param("offset") int offset);


}
