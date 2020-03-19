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
import ru.skillbox.diplom.api.requests.LikeRequest;
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
import ru.skillbox.diplom.services.LikeService;
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
    private LikeService likeService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/{id}")
    public PostResponse getById(@PathVariable int id) {

//        List<Object[]> test = tagsRepositori.tagsForTopic(System.currentTimeMillis());
//        Map<String, Integer> testMap = new HashMap<>();
//        int max = 0;
//        for (Object[] obj : test) {
//            String a = (String) obj[0];
//            BigInteger b = (BigInteger) obj[1];
//            if (max < b.intValue()) max = b.intValue();
//            testMap.put(a, b.intValue());
//        }

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
        logger.info("/api/post");
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.countActualPosts());
        postsResponseAll.setPosts(listPostToResponse(postService.getPosts(offset, mode)));

        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map> addPost(@RequestBody(required = false) PostRequest postRequest) throws ParseException {
        logger.info("/add_Post");

        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);

        Post newPost = new Post();
        newPost.setActive(postRequest.getActive() == 1);
        newPost.seteModerationStatus(EModerationStatus.NEW);
        Optional<User> user = userRepositori.findById(userId);
        if (user.isEmpty()) {
            logger.info("/addPost : пользователь не нашелся в базе - " + userId);
            return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
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

    @PostMapping("/like")
    public ResponseEntity<Map> like(@RequestBody LikeRequest likeRequest) {
        return likeService.like(likeRequest);
    }

    @PostMapping("/dislike")
    public ResponseEntity<Map> dislike(@RequestBody LikeRequest likeRequest) {
        return likeService.dislike(likeRequest);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponseAll> getPostsByTags(@RequestParam int offset, @RequestParam int limit, @RequestParam String tag) {
        logger.info("/api/post/byTag -> " + tag);
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(tagsRepositori.findByText(tag).get().getPosts().size());
        postsResponseAll.setPosts(listPostToResponse(postRepositori.allPostsByTag(tag, System.currentTimeMillis(), offset)));

        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }

    public List<PostResponse> listPostToResponse(List<Post> posts) {
        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : posts) {
            PostResponse postResponse = PostMapper.getPostResponse(post, commentRepositori.findByPostIdOrderByTimeDesc(post.getId()));
            postResponse.setAnnounce(Jsoup.parse(post.getText()).text().substring(0, 100) + "...");
            postResponse.setLikeCount(postRepositori.countLike(post.getId(), 1).orElse(0));
            postResponse.setDislikeCount(postRepositori.countLike(post.getId(), -1).orElse(0));
            postResponseList.add(postResponse);
        }
        return postResponseList;
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponseAll> getPostByDate(@RequestParam int offset, @RequestParam int limit, @RequestParam String date) throws ParseException {
        logger.info("/byDate");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startTime = simpleDateFormat.parse(date).getTime();
        long endTime = simpleDateFormat.parse(date).getTime() + 24 * 60 * 60 * 1000;
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.getCountPostByDate(startTime, endTime));
        postsResponseAll.setPosts(listPostToResponse(postRepositori.getPostByDate(startTime, endTime, offset)));

        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }
//TODO не работает ссылка с фронта для поиска
//    @GetMapping("/search")
//    public ResponseEntity<PostsResponseAll> getPostBySearch(@RequestParam int offset, @RequestParam int limit, @RequestParam String search) {
//        logger.info("/search -> " + search);
//        PostsResponseAll postsResponseAll = new PostsResponseAll();
//        postsResponseAll.setCount(postRepositori.countPostsBySearch(System.currentTimeMillis(), search));
//        postsResponseAll.setPosts(listPostToResponse(postRepositori.getPostBySearch(System.currentTimeMillis(), search)));
//
//        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
//    }
//TODO не работает ссылка с фронта для поиска

    @GetMapping("/my")
    public ResponseEntity<PostsResponseAll> getMyPosts(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String status) {
        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return null;
        Optional<Integer> countMyPosts = Optional.empty();
        String moderationStatus = "";
        logger.info("/my -> userId " + userId + " -> " + status);
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        switch (status) {
            case "inactive":
                countMyPosts = postRepositori.countMyPostsInactive(offset, userId);
                if (countMyPosts.isPresent()) {
                    postsResponseAll.setCount(countMyPosts.get());
                    postsResponseAll.setPosts(listPostToResponse(postRepositori.getMyPostsInactive(offset, userId)));
                    return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
                } else {
                    return null;
                }
            case "pending":
                moderationStatus = "NEW";
                break;
            case "declined":
                moderationStatus = "DECLINED";
                break;
            case "published":
                moderationStatus = "ACCEPTED";
                break;
        }

        countMyPosts = postRepositori.countMyPostsActive(offset, userId, moderationStatus);
        if (countMyPosts.isPresent()) {
            postsResponseAll.setCount(countMyPosts.get());
            postsResponseAll.setPosts(listPostToResponse(postRepositori.getMyPostsActive(offset, userId, moderationStatus)));
            return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
        } else {
            return null;
        }
    }


}
