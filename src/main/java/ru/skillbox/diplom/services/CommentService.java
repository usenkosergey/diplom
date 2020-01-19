package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.entities.Comment;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.CommentRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepositori commentRepositori;

    @Autowired
    private UserRepositori userRepositori;

    public boolean addComment(){
        Comment newComm = new Comment();
        User user = userRepositori.getOne(1);

        newComm.setPostId(1);
        newComm.setUser(user);
        newComm.setText("Comment 1");
        newComm.setTime(System.currentTimeMillis());

        commentRepositori.save(newComm);
        return true;
    }
}
