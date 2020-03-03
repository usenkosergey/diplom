package ru.skillbox.diplom.controllers;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.skillbox.diplom.Mapper.PostMapper;
import ru.skillbox.diplom.api.responses.PostResponse;
import ru.skillbox.diplom.api.responses.PostsResponseAll;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.CommentService;
import ru.skillbox.diplom.services.PostService;
import ru.skillbox.diplom.services.UserService;
import ru.skillbox.diplom.services.VotesService;

import java.math.BigInteger;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ApiPostController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    TagsRepositori tagsRepositori;

    @Autowired
    private PostService postService;


    @GetMapping("/api/post/{id}") //Getting post by ID
    public PostResponse getById(@PathVariable int id) {
        System.out.println("ApiPostController : getById : id - " + id); //TODO удалить позже
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
        if (postRepositori.updateViewCount(id) != 1) {
            logger.error("Update количество просмотров не +1 :" + id);
        }
        return PostMapper.getPostResponse(postRepositori.findById(id).get()); //TODO проверку на существование сделать
    }

    @GetMapping("/api/post")
    public PostsResponseAll getPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode) {
        logger.info("Это ApiPostController метод /api/post");
        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.countActualPosts(System.currentTimeMillis()));
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post : postService.getPosts(offset, mode)) {
            PostResponse postResponse = PostMapper.getPostResponse(post);
            postResponse.setAnnounce(Jsoup.parse(post.getText()).text().substring(0, 150) + "...");
            postResponse.setLikeCount(postRepositori.countLike(post.getId()).orElse(0));
            postResponse.setDislikeCount(postRepositori.countDislike(post.getId()).orElse(0));
            postResponseList.add(postResponse);
        }
        postsResponseAll.setPosts(postResponseList);

        return postsResponseAll;
    }

    @GetMapping("/posts/{mode}")
    public RedirectView redirect(@PathVariable String mode) {
        logger.info("Это ApiPostController метод /posts/{mode}");
        RedirectView rv = new RedirectView("/api/post?offset=0&limit=10&mode=recent");
        rv.setExposeModelAttributes(false);
        return rv;
    }
}
