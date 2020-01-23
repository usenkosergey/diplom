package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.PostVotes;

@Repository
public interface VotesRepositori extends JpaRepository<PostVotes, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM new.post_votes WHERE post_id = :post_id AND value = 1;")
    Integer likeCount(@Param("post_id") int post_id);


    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM new.post_votes WHERE post_id = :post_id AND value = -1;")
    Integer dislikeCount(@Param("post_id") int post_id);
}
