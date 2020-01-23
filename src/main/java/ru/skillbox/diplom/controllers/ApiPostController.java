package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.Post;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.CommentService;
import ru.skillbox.diplom.services.PostService;
import ru.skillbox.diplom.services.UserService;

import java.time.Instant;
import java.time.ZoneId;

@RestController
@RequestMapping("/post")
public class ApiPostController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagsRepositori tagsRepositori;

    @Autowired
    private UserRepositori userRepositori;

    @Autowired
    private PostRepositori postRepositori;

    @GetMapping("/{id}") //Getting post by ID
    public String getById(@PathVariable int id) {
        System.out.println(id);
        userService.addNewUser();
//        postService.addPost();
//        commentService.addComment();

//        Tag tag = new Tag();
//        tag.setText("New");
       // System.out.println(tagsRepositori.addNewTag("Tag2"));
       // System.out.println(tagsRepositori.addNewTag("Tag1"));
//        Tag tag1 = new Tag();
//        tag1.setText("wen");
//        tagsRepositori.save(tag1);
//        Tag tag2 = new Tag();
//        tag2.setText("New-10");
//        tagsRepositori.save(tag2);

        User userEntiti = userRepositori.getOne(1);


        Post newPost = new Post();
        newPost.setActive(true);
        newPost.seteModerationStatus(EModerationStatus.NEW);
        newPost.setModeratorId(1);
        newPost.setUser(userEntiti);
        newPost.setTime(System.currentTimeMillis());
        newPost.setTitle("Title");
        newPost.setText("Hibernate Many to Many Example with Spring Boot");
        newPost.setViewCount(0);

        Tag tag1 = new Tag("Spring Boot");
        Tag tag2 = new Tag("Hibernate");


        // Add tag references in the post
        newPost.getTags().add(tag1);
        newPost.getTags().add(tag2);

        // Add post reference in the tags
        tag1.getPosts().add(newPost);
        tag2.getPosts().add(newPost);

        postRepositori.save(newPost);

        return "PostEntiti";
    }
}
