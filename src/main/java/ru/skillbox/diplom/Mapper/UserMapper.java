package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.UserResponse;
import ru.skillbox.diplom.entities.User;

public class UserMapper {
    public static UserResponse getUser(User user){
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setPhoto(user.getPhoto());
        userResponse.setEmail(user.getEmail());
        userResponse.setModeration(user.getModerator());
        userResponse.setModerationCount(0); //TODO пока руками
        userResponse.setSettings(user.getModerator());

        return userResponse;
    }
}
