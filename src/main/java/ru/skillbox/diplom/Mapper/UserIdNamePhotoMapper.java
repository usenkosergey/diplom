package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.UserIdNamePhoto;
import ru.skillbox.diplom.entities.User;

public class UserIdNamePhotoMapper {
    public static UserIdNamePhoto getUser(User user){
        UserIdNamePhoto userIdNamePhoto = new UserIdNamePhoto();

        userIdNamePhoto.setId(user.getId());
        userIdNamePhoto.setName(user.getName());
        userIdNamePhoto.setPhoto(user.getPhoto());

        return userIdNamePhoto;
    }
}
