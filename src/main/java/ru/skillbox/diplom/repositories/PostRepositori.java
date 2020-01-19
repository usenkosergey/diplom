package ru.skillbox.diplom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.Post;

@Repository
public interface PostRepositori extends PagingAndSortingRepository<Post, Integer> {

}
