package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.services.CommentService;
import ru.skillbox.diplom.services.PostService;
import ru.skillbox.diplom.services.UserService;

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

    @GetMapping("/{id}") //Getting post by ID
    public String getById(@PathVariable int id) {
        System.out.println(id);
//        userService.addNewUser();
//        postService.addPost();
//        commentService.addComment();

//        Tag tag = new Tag();
//        tag.setText("New");
        System.out.println(tagsRepositori.addNewTag("Tag2"));
        System.out.println(tagsRepositori.addNewTag("Tag1"));
//        Tag tag1 = new Tag();
//        tag1.setText("wen");
//        tagsRepositori.save(tag1);
//        Tag tag2 = new Tag();
//        tag2.setText("New-10");
//        tagsRepositori.save(tag2);

        return "PostEntiti";
    }
}
