package ru.skillbox.diplom.services;

import org.hibernate.type.InstantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.api.requests.CommentRequest;
import ru.skillbox.diplom.entities.Comment;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.CommentRepositori;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepositori commentRepositori;

    @Autowired
    private UserRepositori userRepositori;

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public ResponseEntity<Map> addComment(CommentRequest commentRequest) {

        if (commentRequest.getParent_id() != 0 && commentRepositori.findById(commentRequest.getParent_id()).isEmpty()) {
            return new ResponseEntity<>(Constant.responseError("text", "нет такого комментария"), HttpStatus.BAD_REQUEST);
        }

        if (commentRequest.getPost_id() == 0 || postRepositori.findById(commentRequest.getPost_id()).isEmpty()) {
            return new ResponseEntity<>(Constant.responseError("text", "нет такого поста"), HttpStatus.BAD_REQUEST);
        }
        if (commentRequest.getText().length() < 10) {
            return new ResponseEntity<>(Constant.responseError("text", "Текст комментария не задан или слишком короткий (не менее 10 символов).")
                    , HttpStatus.OK);
        }

        Comment newComment = new Comment();
        newComment.setParentId(commentRequest.getParent_id());
        newComment.setPostId(commentRequest.getPost_id());

        Optional<User> user = userRepositori.findById(Constant.auth.get(httpServletRequest.getSession().getId()));
        if (user.isEmpty()) {
            return new ResponseEntity<>(Constant.responseError("text", "нет такого пользователя"), HttpStatus.BAD_REQUEST);
        }
        newComment.setUser(user.get());

        newComment.setText(commentRequest.getText());
        newComment.setTime(System.currentTimeMillis());

        return new ResponseEntity<>(Constant.responseId(commentRepositori.save(newComment).getId()), HttpStatus.OK);
    }
}
