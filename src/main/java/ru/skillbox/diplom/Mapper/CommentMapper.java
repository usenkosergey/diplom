package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.CommentResponse;
import ru.skillbox.diplom.entities.Comment;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommentMapper {
    public static CommentResponse getComment(Comment comment){
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setId(comment.getId());
        commentResponse.setUser(UserMapper.getUser(comment.getUser()));
        commentResponse.setText(comment.getText());
        commentResponse.setTime(Instant.ofEpochMilli(comment.getTime()).atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return commentResponse;
    }
}
