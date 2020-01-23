package ru.skillbox.diplom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Comment;
import ru.skillbox.diplom.entities.Post;

import java.util.List;

@Repository
public interface PostRepositori extends PagingAndSortingRepository<Post, Integer> {

}
