package ru.skillbox.diplom.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.api.responses.InitData;

@RestController
@RequestMapping("/")
public class ApiGeneralController {

    @GetMapping("/api/init")
    public InitData getInit() {
        System.out.println("Это инит - я тут"); //TODO удалить позже
        return InitData.getInitData();
    }
}
