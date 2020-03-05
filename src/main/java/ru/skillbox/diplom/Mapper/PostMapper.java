package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.CommentResponse;
import ru.skillbox.diplom.api.responses.PostResponse;
import ru.skillbox.diplom.entities.Comment;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostMapper {

    public static PostResponse getPostResponse(Post post, List<Comment> comments) {
        PostResponse postResponse = new PostResponse();
        ArrayList<String> tempTag = new ArrayList<>();
        ArrayList<CommentResponse> tempCommetns = new ArrayList<>();

        postResponse.setId(post.getId());
        postResponse.setText(post.getText());
        postResponse.setTime(Instant.ofEpochMilli(post.getTime()).atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        postResponse.setUser(UserIdNameMapper.getUser(post));
        postResponse.setAnnounce("");
        postResponse.setTitle(post.getTitle());
        postResponse.setViewCount(post.getViewCount());

        for (Tag tag : post.getTags()) {
            tempTag.add(tag.getText());
        }
        postResponse.setTags(tempTag);

        for (Comment comment: comments) {
            tempCommetns.add(CommentMapper.getComment(comment));
        }

        postResponse.setComments(tempCommetns);
        postResponse.setCommentCount(comments.size());

        return postResponse;
    }
}
