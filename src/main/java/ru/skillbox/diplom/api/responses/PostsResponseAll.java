package ru.skillbox.diplom.api.responses;

import java.util.List;

public class PostsResponseAll {
    private long count;
    private List<PostResponse> posts;

    public PostsResponseAll() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}
