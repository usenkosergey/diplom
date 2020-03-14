package ru.skillbox.diplom.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.api.requests.CommentRequest;
import ru.skillbox.diplom.api.responses.ResponseAll;
import ru.skillbox.diplom.api.responses.TagsForTopicResponse;
import ru.skillbox.diplom.entities.Settings;
import ru.skillbox.diplom.repositories.SettingsRepositori;
import ru.skillbox.diplom.services.CommentService;
import ru.skillbox.diplom.services.TagService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "mydata")
public class ApiGeneralController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SettingsRepositori settingsRepositori;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

//        @Autowired TODO это почту я проверял, удалить.
//        EMailService eMailService;

    private List<String> initData = new ArrayList<String>();

    public List<String> getinitData() {
        return this.initData;
    }

    @GetMapping("/init")
    public Map getInit() {
        logger.info("/init");

        Map<String, String> initDataResponse = new HashMap<>();
        List<String> tempListInitData = initData;
        for (String data : tempListInitData) {
            String[] tempData = data.split(":");
            initDataResponse.put(tempData[0], tempData[1]);
        }

//        try { //TODO это почту я проверял, удалить.
//            eMailService.sendEmail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        return initDataResponse;
    }

    @GetMapping("/settings")
    public Map getGlobalSettings() {
        logger.info("/settings");
        List<Settings> tempSetting = settingsRepositori.findAll();
        Map<String, Boolean> currentSettings = new HashMap<>();
        for (Settings temp : tempSetting) {
            currentSettings.put(temp.getCode(), temp.getValue().equals("true"));
        }
        return currentSettings;
    }

    @GetMapping("/tag")
    public TagsForTopicResponse getTagsForTopic() {
        return tagService.getTagsForTopic();
    }

    //TODO проверить что вышел юзер
    @PostMapping("/profile/my")
    public ResponseAll profile(@RequestParam(value = "photo", required = false) MultipartFile uploadfile,
                               @RequestParam(value = "name", required = false) String name
    ) throws IOException {
        logger.info("/profile/my");

//        byte[] bytes = uploadfile.getBytes();
//        Path path = Paths.get("./src/main/resources/static/img/" + uploadfile.getOriginalFilename());
//        Files.write(path, bytes);

        return new ResponseAll(false, "photo", "Фото слишком большое, нужно не более 5 Мб");//TODO доделать
    }

    @PostMapping("/image")//TODO пока очень сыро
    public String uploadFile(@RequestParam(value = "image", required = false) MultipartFile uploadfile) throws IOException {
        logger.info("/api/image");
        byte[] bytes = uploadfile.getBytes();
        String aaa = "/upload/ab/cd/ef/";
        Path path = Paths.get("./src/main/resources/static" + aaa + uploadfile.getOriginalFilename());
        Files.write(path, bytes);
        return aaa + uploadfile.getOriginalFilename();
    }

    @PostMapping("/comment")
    public ResponseEntity<Map> comment(@RequestBody CommentRequest commentRequest) {
        logger.info("/comment");
        return commentService.addComment(commentRequest);
    }


}
