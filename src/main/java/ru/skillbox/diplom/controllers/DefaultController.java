package ru.skillbox.diplom.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\.]*}")
    public ResponseEntity<String> index() {
        logger.info("Дефолтный контроллер");
        return new ResponseEntity<>("forward:/", HttpStatus.OK);
    }
}
