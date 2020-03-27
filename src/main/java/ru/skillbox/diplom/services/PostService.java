package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;


import java.util.List;

@Service
@Transactional
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private UserRepositori userRepositori;

    @Autowired
    private TagsRepositori tagsRepositori;


    public boolean addPost() {

        return true; //TODO нужно переделать
    }

    public List<Post> getPosts(int offset, String mode, int limit) {
        Pageable pageable = null;
        if (mode.equals("recent")) {
            pageable = PageRequest.of(offset / limit, limit, Sort.Direction.DESC, "time");
            return postRepositori.getListPostsRecentOrEarly(pageable, System.currentTimeMillis());
        } else if (mode.equals("early")) {
            pageable = PageRequest.of(offset / limit, limit, Sort.Direction.ASC, "time");
            return postRepositori.getListPostsRecentOrEarly(pageable, System.currentTimeMillis());
        } else if (mode.equals("best")) {
            pageable = PageRequest.of(offset / limit, limit, Sort.Direction.DESC, "countLike");
            return postRepositori.getListBestPosts(pageable, System.currentTimeMillis());
        } else if (mode.equals("popular")) {
            pageable = PageRequest.of(offset / limit, limit, Sort.Direction.DESC, "countComm");
            //System.out.println("сортировка комментами"); //TODO удалить позже
            return postRepositori.getListCommentPosts(pageable, System.currentTimeMillis());
        }
        logger.error("не вернулась сортировка, никакая");
        return null;
    }
}
