package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.Mapper.PostMapper;
import ru.skillbox.diplom.api.responses.PostResponse;
import ru.skillbox.diplom.entities.Tag;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.TagsRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.CommentService;
import ru.skillbox.diplom.services.PostService;
import ru.skillbox.diplom.services.UserService;
import ru.skillbox.diplom.services.VotesService;

@RestController
@RequestMapping("/post")
public class ApiPostController {

    @Autowired
    private PostRepositori postRepositori;

    @GetMapping ("/{id}") //Getting post by ID
    public PostResponse getById(@PathVariable int id) {
        System.out.println("ApiPostController : getById : id - " + id); //TODO удалить позже

//        //TODO добавление лайка или дизлайка к записи, потом убиру от сюда
//        //TODO нужно еще как то обыграть ошибку вставки лайка
//        //TODO и -1 заменить в зависимости лайк это или нет
//        if (!votesService.addVote(1, (byte) -1))
//            votesService.addVote(1, (byte) -1);

        return PostMapper.getPostResponse(postRepositori.findById(id).get()); //TODO проверку на существование сделать
    }
}
