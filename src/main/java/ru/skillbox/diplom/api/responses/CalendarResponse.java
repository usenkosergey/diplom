package ru.skillbox.diplom.api.responses;

import java.util.ArrayList;
import java.util.Map;

public class CalendarResponse {

    private ArrayList<Integer> years;
    private Map<String, Integer> posts;

    public CalendarResponse() {
    }

    public ArrayList<Integer> getYears() {
        return years;
    }

    public void setYears(ArrayList<Integer> years) {
        this.years = years;
    }

    public Map<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Integer> posts) {
        this.posts = posts;
    }
}
