package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.UserFullResponse;
import ru.skillbox.diplom.api.responses.UserResponse;
import ru.skillbox.diplom.entities.User;

public class UserMapper {
    public static UserResponse getUser(User user){
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setPhoto(user.getPhoto());

        return userResponse;
    }

    public static UserFullResponse getFullUser(User user){
        UserFullResponse userFullResponse = new UserFullResponse();

        userFullResponse.setId(user.getId());
        userFullResponse.setName(user.getName());
        userFullResponse.setPhoto(user.getPhoto());
        userFullResponse.setEmail(user.getEmail());
        userFullResponse.setModeration(user.getModerator());
        userFullResponse.setModerationCount(0); //TODO пока руками
        userFullResponse.setSettings(user.getModerator());

        return userFullResponse;
    }
}
