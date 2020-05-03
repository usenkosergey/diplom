package ru.skillbox.diplom.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.api.requests.ProfileRequest;
import ru.skillbox.diplom.api.responses.ResponseAll;
import ru.skillbox.diplom.config.StorageConfig;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.ProfileService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/api/profile")
public class ProfileEditController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserRepositori userRepositori;

    @Autowired
    private StorageConfig storageConfig;

    @Autowired
    private ProfileService profileService;


    @PostMapping(
            value = "/my",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseAll> profile(
            @RequestParam(value = "photo", required = false) MultipartFile multipartFile,
            @ModelAttribute ProfileRequest profileRequest
    ) throws IOException {
        logger.info("/profile/my");
        Path path = null;

        if (multipartFile != null) {
            if (multipartFile.getSize() >= 5242880) return
                    new ResponseEntity<>(new ResponseAll(false, "photo", "Фото слишком большое, нужно не более 5 Мб"), HttpStatus.OK);
            byte[] bytes = multipartFile.getBytes();
            String[] uploadName = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.");
            String avatarName = Constant.codeGenerator(10) + "." + uploadName[uploadName.length - 1];
            path = Paths.get(storageConfig.getLocation() + "/" + avatarName);
            Files.write(path, bytes);
        }
        return profileService.editProfile(profileRequest, path);
    }

    @PostMapping("/my")
    public ResponseEntity<ResponseAll> profile(@RequestBody(required = false) ProfileRequest profileRequest) throws IOException {
        logger.info("/profile/my");

        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return profileService.editProfile(profileRequest, null);
    }
}
