package ru.skillbox.diplom.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String getHomePage() {
        System.out.println("тут работает");
        return "index";
    }
}
