package ru.skillbox.diplom.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.api.requests.CommentRequest;
import ru.skillbox.diplom.api.requests.PostStatusRequest;
import ru.skillbox.diplom.api.requests.SettingsRequest;
import ru.skillbox.diplom.api.responses.CalendarResponse;
import ru.skillbox.diplom.api.responses.ResponseAll;
import ru.skillbox.diplom.api.responses.TagsForTopicResponse;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Settings;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.SettingsRepositori;
import ru.skillbox.diplom.repositories.VotesRepositori;
import ru.skillbox.diplom.services.CommentService;
import ru.skillbox.diplom.services.SettingsService;
import ru.skillbox.diplom.services.TagService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private VotesRepositori votesRepositori;

    @Autowired
    private SettingsService settingsService;

    private List<String> initData = new ArrayList<String>();

    public List<String> getInitData() {
        return this.initData;
    }

    @GetMapping("/init")
    public Map<String, String> getInit() {
        logger.info("/init");
        Map<String, String> initDataResponse = new HashMap<>();
        List<String> tempListInitData = initData;
        for (String data : tempListInitData) {
            String[] tempData = data.split(":");
            initDataResponse.put(tempData[0], tempData[1]);
        }
        return initDataResponse;
    }

    @GetMapping("/settings")
    public Map<String, Boolean> getGlobalSettings() {
        logger.info("/settings");

        Map<String, Boolean> currentSettings = new HashMap<>();
        for (Settings temp : settingsRepositori.findAll()) {
            currentSettings.put(temp.getCode(), temp.getValue());
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

    @PostMapping("/image")
    public String uploadFile(@RequestParam(value = "image", required = false) MultipartFile uploadfile) throws IOException {
        logger.info("/api/image");
        if (uploadfile.isEmpty()) return null;
        byte[] bytes = uploadfile.getBytes();
        String[] uploadName = Objects.requireNonNull(uploadfile.getOriginalFilename()).split("\\.");
        String newFileName = Constant.codeGenerator(10) + "." + uploadName[uploadName.length - 1];
        Path path = Paths.get("./src/main/resources/static/upload/" + newFileName);
        Files.write(path, bytes);
        return "/upload/" + newFileName;
    }

    @PostMapping("/comment")
    public ResponseEntity<Map> comment(@RequestBody(required = false) CommentRequest commentRequest) {
        logger.info("/comment");
        return commentService.addComment(commentRequest);
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<Map> statisticsAll() {
        logger.info("/statistics/all");
        Optional<Settings> settings = settingsRepositori.findById(3);
        if (settings.get().getValue() | !Constant.auth.isEmpty()) {

            logger.info("statistics for all");
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("postsCount", postRepositori.count());
            statistics.put("likesCount", votesRepositori.countByValue(1));
            statistics.put("dislikesCount", votesRepositori.countByValue(-1));
            statistics.put("viewsCount", postRepositori.sumByViewCount());
            statistics.put("firstPublication",
                    Instant.ofEpochMilli(postRepositori.firstPublication().get().getTime()).atZone(ZoneId.systemDefault())
                            .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        }
        logger.info("statistics NOT for all");
        return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> calendar(@RequestParam(required = false) Integer year) {
        logger.info("/calendar ->" + year);
        CalendarResponse calendarResponse = new CalendarResponse();
        List<String> yearListString = postRepositori.getYearToCalendar(System.currentTimeMillis());
        ArrayList<Integer> yearListInteger = new ArrayList<>();
        for (String yearStr : yearListString) {
            yearListInteger.add(Integer.parseInt(yearStr));
        }
        calendarResponse.setYears(yearListInteger);
        List<Object[]> postForYears = postRepositori.listPostForYears(System.currentTimeMillis(), year);
        Map<String, Integer> tempMap = new HashMap<>();
        for (Object[] obj : postForYears) {
            String objString = (String) obj[0];
            BigInteger objBigInteger = (BigInteger) obj[1];
            tempMap.put(objString, objBigInteger.intValue());
        }
        calendarResponse.setPosts(tempMap);
        return new ResponseEntity<>(calendarResponse, HttpStatus.OK);
    }

    @GetMapping("/statistics/my")
    public ResponseEntity<Map<String, Object>> statisticsMy() {
        logger.info("/statistics/my");
        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return null;

        Map<String, Object> myStatistics = new HashMap<>();
        Integer postsCount = postRepositori.countByUser(userId).orElse(0);
        if (postsCount != 0) {
            myStatistics.put("postsCount", postsCount);
            myStatistics.put("likesCount", votesRepositori.countByValueAndUserId(1, userId).orElse(0));
            myStatistics.put("dislikesCount", votesRepositori.countByValueAndUserId(-1, userId).orElse(0));
            myStatistics.put("viewsCount", postRepositori.sumMyViewCount(userId).orElse(0));
            myStatistics.put("firstPublication",
                    Instant.ofEpochMilli(postRepositori.firstMyPublication(userId).get().getTime()).atZone(ZoneId.systemDefault())
                            .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            myStatistics.put("postsCount", 0);
            myStatistics.put("likesCount", 0);
            myStatistics.put("dislikesCount", 0);
            myStatistics.put("viewsCount", 0);
            myStatistics.put("firstPublication", "все впереди!");
        }
        return new ResponseEntity<>(myStatistics, HttpStatus.OK);
    }

    @PutMapping("/settings")
    public void settings(@RequestBody(required = false) SettingsRequest settingsRequest) {
        logger.info("/settings");
        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId != 0) {
            logger.info("/settings -> " + userId);
            settingsService.changeSettings(settingsRequest);
        }
    }

    @PostMapping("/moderation")
    public void postStatusChange(@RequestBody(required = false) PostStatusRequest postStatusRequest) {
        logger.info("/moderation - start, postID " + postStatusRequest.getPost_id());
        if (!Constant.auth.isEmpty()) {
            int userId = Constant.userId(httpServletRequest.getSession().getId());
            Post post = postRepositori.findById(postStatusRequest.getPost_id()).orElse(new Post());
            logger.info("postId - " + post.getId() + ", start status - " + post.geteModerationStatus().toString());
            if (postStatusRequest.getDecision().equals("decline")) {
                post.seteModerationStatus(EModerationStatus.DECLINED);
            } else {
                post.seteModerationStatus(EModerationStatus.ACCEPTED);
            }
            post.setModeratorId(userId);
            post = postRepositori.save(post);
            logger.info("postId - " + post.getId() + ", end status - " + post.geteModerationStatus().toString());
            logger.info("/moderation - end, postId " + postStatusRequest.getPost_id());
        }
    }
}
