package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.PostResponse;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PostMapper {

    public static PostResponse getPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        ArrayList<String> tempTag = new ArrayList<>();
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



        return postResponse;
    }
}
