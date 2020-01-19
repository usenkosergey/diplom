package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;

import java.time.LocalDate;
import java.util.Date;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private UserRepositori userRepositori;


    public boolean addPost() {
        User userEntiti = userRepositori.getOne(1);

        Post newPost = new Post();
        newPost.setActive(true);
        newPost.seteModerationStatus(EModerationStatus.NEW);
        newPost.setModeratorId(1);
        newPost.setUser(userEntiti);
        newPost.setTime(System.currentTimeMillis());
        newPost.setTitle("wewqewqe");
        newPost.setText("yyyyyyyyyyyyyyyyy");
        newPost.setViewCount(0);

        postRepositori.save(newPost);
        return true;
    }
}
