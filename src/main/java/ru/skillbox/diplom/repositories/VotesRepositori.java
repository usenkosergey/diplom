package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.PostVotes;

import java.util.Optional;

@Repository
public interface VotesRepositori extends JpaRepository<PostVotes, Integer> {

    @Query(
    value = "FROM PostVotes AS PV " +
            "WHERE PV.postId = (:post_id) " +
            "AND PV.userId = (:user_id)")
    Optional<PostVotes> likeForPost(@Param("post_id") int post_id,
                                    @Param("user_id") int user_id);

    long countByValue (Integer value);

    Optional<Integer> countByValueAndUserId (Integer value, Integer id);

}
