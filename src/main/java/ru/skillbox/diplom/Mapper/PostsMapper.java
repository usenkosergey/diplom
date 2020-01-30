package ru.skillbox.diplom.Mapper;

import ru.skillbox.diplom.api.responses.PostsResponseAll;
import ru.skillbox.diplom.entities.Post;

import java.util.List;

public class PostsMapper {
    public static PostsResponseAll getAllPosts(List<Post> posts, long countPosts){
        PostsResponseAll postsResponseAll = new PostsResponseAll();

        postsResponseAll.setCount(countPosts);


        return postsResponseAll;
    }
}
