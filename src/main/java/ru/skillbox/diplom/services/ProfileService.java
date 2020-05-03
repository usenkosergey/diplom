package ru.skillbox.diplom.services;

import org.jsoup.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.api.requests.ProfileRequest;
import ru.skillbox.diplom.api.responses.ResponseAll;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.UserRepositori;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ProfileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserRepositori userRepositori;

    public ResponseEntity<ResponseAll> editProfile(ProfileRequest profileRequest, Path path){
        int userId = Constant.userId(httpServletRequest.getSession().getId());
        Optional<User> currentUser = userRepositori.findById(userId);
        if (currentUser.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (profileRequest.getRemovePhoto() == 1) currentUser.get().setPhoto("");
        if (!profileRequest.getName().equals(currentUser.get().getName()))
            currentUser.get().setName(profileRequest.getName());

        if (!StringUtil.isBlank(profileRequest.getPassword())) {
            currentUser.get().setPassword(new BCryptPasswordEncoder().encode(profileRequest.getPassword()));
        }

        if (!profileRequest.getEmail().equals(currentUser.get().getEmail())) {
            if (userRepositori.findByEmail(profileRequest.getEmail()).isEmpty()) {
                currentUser.get().setEmail(profileRequest.getEmail());
            } else {
                return new ResponseEntity<>(new ResponseAll(false, "email", "Этот e-mail уже зарегистрирован"), HttpStatus.OK);
            }
        }
        if (path != null) {
            currentUser.get().setPhoto("\\" + path.toString());
        }

        return new ResponseEntity<>(new ResponseAll(true), HttpStatus.OK);
    }
}
