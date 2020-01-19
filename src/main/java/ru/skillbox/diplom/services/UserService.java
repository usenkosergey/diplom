package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.UserRepositori;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepositori userRepositori;

    public boolean addNewUser() {
        User newUser = new User();
        newUser.setModerator(true);
        newUser.setName("Sergey");
        newUser.setEmail("www@sss.ru");
        newUser.setPassword("10000");
        newUser.setRegTime(System.currentTimeMillis());
        userRepositori.save(newUser);

        return true;
    }
}
