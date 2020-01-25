package ru.skillbox.diplom.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String index() {
        System.out.println("я в DefaultController"); //TODO удалить позже
        return "index";
    }
}
