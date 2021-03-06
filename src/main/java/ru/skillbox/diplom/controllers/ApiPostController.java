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
import ru.skillbox.diplom.entities.*;
import ru.skillbox.diplom.repositories.CommentRepositori;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.LikeService;
import ru.skillbox.diplom.services.PostService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<PostResponse> getById(@PathVariable(required = false) int id) {

        Optional<Post> post = postRepositori.findById(id);
        if (post.isPresent()) {
            PostResponse postResponse = PostMapper.getPostResponse(post.get(), commentRepositori.findByPostIdOrderByTimeDesc(id));
            postResponse.setLikeCount(postRepositori.countLike(id, 1).orElse(0));
            postResponse.setDislikeCount(postRepositori.countLike(id, -1).orElse(0));
            if (postRepositori.updateViewCount(id) != 1) {
                logger.error("Update количество просмотров не +1 :" + id);
            }
            return new ResponseEntity<>(postResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("")
    public ResponseEntity<PostsResponseAll> getPosts(@RequestParam(required = false) int offset,
                                                     @RequestParam(required = false) int limit,
                                                     @RequestParam(required = false) String mode) {
        logger.info("/api/post");

        PostsResponseAll postsResponseAll = new PostsResponseAll();
        if (mode.equals("recent") ||
                mode.equals("popular") ||
                mode.equals("best") ||
                mode.equals("early")) {
            postsResponseAll.setCount(postRepositori.countActualPosts(System.currentTimeMillis()));
            postsResponseAll.setPosts(listPostToResponse(postService.getPosts(offset, mode, limit)));
            return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
        }
        return new ResponseEntity<>(postsResponseAll, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("")
    public ResponseEntity<Map> addPost(@RequestBody(required = false) PostRequest postRequest) throws ParseException {
        logger.info("/add_Post");
        //System.out.println(postRequest.getTime());
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        long timeLong = Timestamp.valueOf(LocalDateTime.parse(postRequest.getTime(), formatter)).getTime();

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
    public ResponseEntity<Map> like(@RequestBody(required = false) LikeRequest likeRequest) {
        return likeService.like(likeRequest);
    }

    @PostMapping("/dislike")
    public ResponseEntity<Map> dislike(@RequestBody(required = false) LikeRequest likeRequest) {
        return likeService.dislike(likeRequest);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponseAll> getPostsByTags(@RequestParam(required = false) int offset,
                                                           @RequestParam(required = false) int limit,
                                                           @RequestParam(required = false) String tag) {
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
    public ResponseEntity<PostsResponseAll> getPostByDate(@RequestParam(required = false) int offset,
                                                          @RequestParam(required = false) int limit,
                                                          @RequestParam(required = false) String date) throws ParseException {
        logger.info("/byDate");
        Pageable pageable = PageRequest.of(offset / limit, limit);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startTime = simpleDateFormat.parse(date).getTime();
        long endTime = simpleDateFormat.parse(date).getTime() + 24 * 60 * 60 * 1000;
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.countPostByDate(startTime, endTime));
        postsResponseAll.setPosts(listPostToResponse(postRepositori.getPostByDate(pageable, startTime, endTime)));

        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<PostsResponseAll> getPostBySearch(@RequestParam int offset, @RequestParam int limit, @RequestParam String query) {
        logger.info("/search -> " + query);
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.countPostsBySearch(System.currentTimeMillis(), query));
        postsResponseAll.setPosts(listPostToResponse(postRepositori.getPostBySearch(pageable, System.currentTimeMillis(), query)));

        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<PostsResponseAll> getMyPosts(@RequestParam(required = false) int offset,
                                                       @RequestParam(required = false) int limit,
                                                       @RequestParam(required = false) String status) {
        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return null;

        User user = new User();
        user.setId(userId);

        Pageable pageable = PageRequest.of(offset / limit, limit);

        Optional<Integer> countMyPosts = Optional.empty();
        EModerationStatus eModerationStatus = null;
        logger.info("/my -> userId " + userId + " -> " + status);
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        switch (status) {
            case "inactive":
                countMyPosts = postRepositori.countMyPostsInactive(user);
                if (countMyPosts.isPresent()) {
                    postsResponseAll.setCount(countMyPosts.get());
                    postsResponseAll.setPosts(listPostToResponse(postRepositori.getMyPostsInactive(user, pageable)));
                    return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
                } else {
                    return null;
                }
            case "pending":
                eModerationStatus = EModerationStatus.NEW;
                break;
            case "declined":
                eModerationStatus = EModerationStatus.DECLINED;
                break;
            case "published":
                eModerationStatus = EModerationStatus.ACCEPTED;
                break;
        }

        countMyPosts = postRepositori.countMyPostsActive(user, eModerationStatus);
        if (countMyPosts.isPresent()) {
            postsResponseAll.setCount(countMyPosts.get());
            postsResponseAll.setPosts(listPostToResponse(postRepositori.getMyPostsActive(user, eModerationStatus, pageable)));
            return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map> putPost(@RequestBody(required = false) PostRequest postRequest,
                                       @PathVariable(required = false) int id) {

        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return null;

        Post putPost = postRepositori.findById(id).get();

        putPost.setActive(postRequest.getActive() == 1);
        User user = userRepositori.findById(userId).get();
        if (!user.getModerator()) {
            putPost.seteModerationStatus(EModerationStatus.NEW);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        long timeLong = Timestamp.valueOf(LocalDateTime.parse(postRequest.getTime(), formatter)).getTime();

        if (timeLong < System.currentTimeMillis()) timeLong = System.currentTimeMillis();
        putPost.setTime(timeLong);


        if (postRequest.getTitle().length() < 10)
            return new ResponseEntity<>(Constant.responseError("title", "Заголовок не установлен или короткий (не менее 10 символов)"),
                    HttpStatus.OK);
        putPost.setTitle(postRequest.getTitle());

        if (postRequest.getText().length() < 500)
            return new ResponseEntity<>(Constant.responseError("text", "Текст публикации слишком короткий (не менее 500 символов)"),
                    HttpStatus.OK);
        putPost.setText(postRequest.getText());

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
        putPost.setTags(addPostTag);
        postRepositori.save(putPost);

        return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
    }

    @GetMapping("/moderation")
    public ResponseEntity<PostsResponseAll> getModerationPosts(@RequestParam(required = false) int offset,
                                                               @RequestParam(required = false) int limit,
                                                               @RequestParam(required = false) String status) {
        int userId = Constant.userId(httpServletRequest.getSession().getId());
        if (userId == 0) return null;
        Optional<User> currentUser = userRepositori.findById(userId);
        if (!currentUser.get().getModerator()) return null;

        Pageable pageable = PageRequest.of(offset / limit, limit);

        logger.info("/moderation -> " + userId + ", status -> " + status);
        String moderationStatus = "";
        Optional<Long> countPosts = Optional.empty();
        PostsResponseAll postsResponseAll = new PostsResponseAll();

        switch (status) {
            case "new":
                countPosts = postRepositori.countNewPosts();
                if (countPosts.isPresent()) {
                    postsResponseAll.setCount(countPosts.orElse(0L));
                    postsResponseAll.setPosts(listPostToResponse(postRepositori.getNewPostsForModeration(pageable)));
                    return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
                }
            case "declined":
                moderationStatus = "DECLINED";
                break;
            case "accepted":
                moderationStatus = "ACCEPTED";
                break;
        }

        countPosts = postRepositori.countModerationPosts(moderationStatus, userId);
        postsResponseAll.setCount(countPosts.orElse(0L));
        postsResponseAll.setPosts(listPostToResponse(postRepositori.getModerationPosts(moderationStatus, userId, pageable)));
        return new ResponseEntity<>(postsResponseAll, HttpStatus.OK);
    }

}
