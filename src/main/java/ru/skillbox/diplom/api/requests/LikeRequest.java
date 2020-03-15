package ru.skillbox.diplom.api.requests;

public class LikeRequest {

    private int post_id;

    public LikeRequest() {
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
