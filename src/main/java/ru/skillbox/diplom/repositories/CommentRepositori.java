package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepositori extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostIdOrderByTimeDesc(int postId);
}
