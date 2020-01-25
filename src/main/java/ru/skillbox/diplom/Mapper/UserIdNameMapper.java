package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.UserIdName;
import ru.skillbox.diplom.entities.Post;

public class UserIdNameMapper {
    public static UserIdName getUser(Post post) {
        UserIdName userIdName = new UserIdName();

        userIdName.setId(post.getUser().getId());
        userIdName.setName(post.getUser().getName());

        return userIdName;
    }
}
