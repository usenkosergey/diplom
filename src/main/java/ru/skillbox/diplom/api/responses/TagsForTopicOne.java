package ru.skillbox.diplom.api.responses;

import java.util.List;

public class TagsForTopicOne {
    private int id;
    private String name;
    private double weight;

    public TagsForTopicOne() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
