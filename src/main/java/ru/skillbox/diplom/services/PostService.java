package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.entities.User;
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

    public List<Post> getPosts(int offset, String mode) {
        if (mode.equals("recent")) {
            //System.out.println("сортировка в прямом порядке"); //TODO удалить позже
            return postRepositori.getListRecentPosts(offset);
        } else if (mode.equals("early")) {
            //System.out.println("сортировка в обратном порядке"); //TODO удалить позже
            return postRepositori.getListEarlyPosts(System.currentTimeMillis(), offset);
        } else if (mode.equals("best")) {
            //System.out.println("сортировка Бест"); //TODO удалить позже
            return postRepositori.getListBestPosts(System.currentTimeMillis(),offset);
        } else if (mode.equals("popular")) {
            //System.out.println("сортировка комментами"); //TODO удалить позже
            return postRepositori.getListCommentPosts(System.currentTimeMillis(), offset);
        }
        logger.error("не вернулась сортировка, никакая");
        return null;
    }
}
