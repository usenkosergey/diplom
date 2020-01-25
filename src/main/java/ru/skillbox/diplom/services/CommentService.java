package ru.skillbox.diplom.services;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentService {

    public boolean addComment(){
//        Comment newComm = new Comment();
//        User user = userRepositori.getOne(1);
//
//        newComm.setPostId(1);
//        newComm.setUser(user);
//        newComm.setText("Comment 1");
//        newComm.setTime(System.currentTimeMillis());
//
//        commentRepositori.save(newComm);
        return true; //TODO нужно переделать
    }
}
