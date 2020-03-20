package ru.skillbox.diplom.api.requests;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PostRequest {
    private LocalDateTime time;
    private int active;
    private String title;
    private String text;
    private ArrayList<String> tags;

    public PostRequest() {
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
