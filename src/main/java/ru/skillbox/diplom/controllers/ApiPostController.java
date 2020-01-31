package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
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
        return PostMapper.getPostResponse(postRepositori.findById(id).get()); //TODO проверку на существование сделать
    }

    @GetMapping("/api/post")
    public PostsResponseAll getPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode) {
        System.out.println("getPosts ---- ");

        PostsResponseAll postsResponseAll = new PostsResponseAll();
        postsResponseAll.setCount(postRepositori.count());
        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : postService.getPosts()) {
            //PostResponse postResponse = new PostResponse();

            postResponseList.add(PostMapper.getPostResponse(post));

        }
        postsResponseAll.setPosts(postResponseList);

        return postsResponseAll;
    }
}
