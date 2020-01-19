package ru.skillbox.diplom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.repositories.TagsRepositori;

import javax.transaction.Transactional;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagsRepositori tagsRepositori;


}
