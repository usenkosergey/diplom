package ru.skillbox.diplom.controllers;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.Mapper.PostMapper;
import ru.skillbox.diplom.api.requests.PostRequest;
import ru.skillbox.diplom.api.responses.PostResponse;
import ru.skillbox.diplom.api.responses.PostsResponseAll;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.CommentRepositori;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.PostService;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private UserRepositori userRepositori;

    @Autowired
    private TagsRepositori tagsRepositori;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepositori commentRepositori;

    @Autowired
    private HttpServletRequest httpServletRequest;

//    @Autowired
//    private HttpServletRequest reg;//TODO

    @GetMapping("/{id}") //Getting post by ID
    public PostResponse getById(@PathVariable int id) {
        //System.out.println("ApiPostController : getById : id - " + id); //TODO удалить позже
        //tagsRepositori.addNewTag("asdfg");
        //TODO удаление тегов
        //System.out.println(postRepositori.findById(2).get().getText().replaceAll("\\<.*?\\>", ""));
//        //TODO добавление лайка или дизлайка к записи, потом убиру от сюда
//        //TODO нужно еще как то обыграть ошибку вставки лайка
//        //TODO и -1 заменить в зависимости лайк это или нет
//        if (!votesService.addVote(1, (byte) -1))
//            votesService.addVote(1, (byte) -1);


//        List<Object[]> test = tagsRepositori.tagsForTopic(System.currentTimeMillis());
//        Map<String, Integer> testMap = new HashMap<>();
//        int max = 0;
//        for (Object[] obj : test) {
//            String a = (String) obj[0];
//            BigInteger b = (BigInteger) obj[1];
//            if (max < b.intValue()) max = b.intValue();
//            testMap.put(a, b.intValue());
//        }
//        System.out.println("max --- " + max);
//
//        System.out.println("--------");
        PostResponse postResponse = PostMapper.getPostResponse(postRepositori.findById(id).get(), commentRepositori.findByPostIdOrderByTimeDesc(id));
        postResponse.setLikeCount(postRepositori.countLike(id, 1).orElse(0));
        postResponse.setDislikeCount(postRepositori.countLike(id, -1).orElse(0));
        if (postRepositori.updateViewCount(id) != 1) {
            logger.error("Update количество просмотров не +1 :" + id);
        }
        return postResponse; //TODO проверку на существование сделать
    }

    @GetMapping("")
    public ResponseEntity<PostsResponseAll> getPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode) {
        logger.info("Это ApiPostController метод /api/post");
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.countActualPosts(System.currentTimeMillis()));
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post : postService.getPosts(offset, mode)) {
            PostResponse postResponse = PostMapper.getPostResponse(post, commentRepositori.findByPostIdOrderByTimeDesc(post.getId()));
            postResponse.setAnnounce(Jsoup.parse(post.getText()).text().substring(0, 150) + "...");
            postResponse.setLikeCount(postRepositori.countLike(post.getId(), 1).orElse(0));
            postResponse.setDislikeCount(postRepositori.countLike(post.getId(), -1).orElse(0));
            postResponseList.add(postResponse);
        }
        postsResponseAll.setPosts(postResponseList);

//        System.out.println("--- " + reg.getSession().getId());
//        System.out.println("--- " + reg.getServletPath());

        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map> addPost(@RequestBody PostRequest postRequest) throws ParseException {
        logger.info("add Post");
        Post newPost = new Post();

        newPost.setActive(postRequest.getActive() == 1);
        newPost.seteModerationStatus(EModerationStatus.NEW);

        if (Constant.auth.size() == 0)
            return new ResponseEntity<>(Constant.responseError("text", "пользователя нет"), HttpStatus.BAD_REQUEST);
        Optional<User> user = userRepositori.findById(Constant.auth.get(httpServletRequest.getSession().getId()));
        if (user.isEmpty()) {
            logger.info("/addPost : пользователь не нашелся в базе - " + Constant.auth.get(httpServletRequest.getSession().getId()));
        }
        newPost.setUser(user.get());

        String timeString = postRequest.getTime();
        timeString = timeString.replace('T', ' ');
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeLong = simpleDateFormat.parse(timeString).getTime();
        if (timeLong < System.currentTimeMillis()) timeLong = System.currentTimeMillis();
        newPost.setTime(timeLong);

        if (postRequest.getTitle().length() < 10)
            return new ResponseEntity<>(Constant.responseError("title", "Заголовок не установлен или короткий (не менее 10 символов)"),
                    HttpStatus.OK);
        newPost.setTitle(postRequest.getTitle());

        if (postRequest.getText().length() < 500)
            return new ResponseEntity<>(Constant.responseError("text", "Текст публикации слишком короткий (не менее 500 символов)"),
                    HttpStatus.OK);
        newPost.setText(postRequest.getText());
        newPost.setViewCount(0);

        ArrayList<String> requestTags = postRequest.getTags();
        ArrayList<Tag> addPostTag = new ArrayList<>();

        for (String tag : requestTags) {
            Optional<Tag> tempTag = tagsRepositori.findByText(tag);
            if (tempTag.isPresent()) {
                addPostTag.add(tempTag.get());
            } else {
                Tag tagNew = new Tag();
                tagNew.setText(tag);
                addPostTag.add(tagsRepositori.save(tagNew));
            }
        }
        newPost.setTags(addPostTag);
        postRepositori.save(newPost);

        return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
    }

}
