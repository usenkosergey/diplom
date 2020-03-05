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
        User userEntiti = userRepositori.getOne(1);

        Post newPost = new Post();
        newPost.setActive(true);
        newPost.seteModerationStatus(EModerationStatus.NEW);
        newPost.setModeratorId(1);
        newPost.setUser(userEntiti);
        newPost.setTime(System.currentTimeMillis());
        newPost.setTitle("wewqewqe");
        newPost.setText("Hibernate Many to Many Example with Spring Boot");
        newPost.setViewCount(0);

        Tag tag1 = tagsRepositori.findByText("Test_tag_14").isEmpty()
                ? new Tag("Test_tag_14")
                : tagsRepositori.getOne(tagsRepositori.findByText("Test_tag_14").get().getId());

        Tag tag2 = tagsRepositori.findByText("Spring Boot_12").isEmpty()
                ? new Tag("Spring Boot_12")
                : tagsRepositori.getOne(tagsRepositori.findByText("Spring Boot_12").get().getId());

        newPost.getTags().add(tag1);
        newPost.getTags().add(tag2);
        //Add post reference in the tags
        tag1.getPosts().add(newPost);
        tag2.getPosts().add(newPost);

        postRepositori.save(newPost);
        return true; //TODO нужно переделать
    }

    public List<Post> getPosts(int offset, String mode) {
        if (mode.equals("recent")) {
            System.out.println("сортировка в прямом порядке"); //TODO удалить позже
            return postRepositori.getListRecentPosts(System.currentTimeMillis(), offset);
        } else if (mode.equals("early")) {
            System.out.println("сортировка в обратном порядке"); //TODO удалить позже
            return postRepositori.getListEarlyPosts(System.currentTimeMillis(), offset);
        } else if (mode.equals("best")) {
            System.out.println("сортировка Бест"); //TODO удалить позже
            return postRepositori.getListBestPosts(System.currentTimeMillis(),offset);
        } else if (mode.equals("popular")) {
            System.out.println("сортировка комментами"); //TODO удалить позже
            return postRepositori.getListCommentPosts(System.currentTimeMillis(), offset);
        }
        logger.error("не вернулась сортировка, никакая");

        return null;
    }
}
