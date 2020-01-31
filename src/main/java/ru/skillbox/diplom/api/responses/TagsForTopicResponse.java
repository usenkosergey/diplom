package ru.skillbox.diplom.api.responses;

import java.util.List;

public class TagsForTopicResponse {
    List<TagsForTopicOne> tags;

    public TagsForTopicResponse() {
    }

    public List<TagsForTopicOne> getTags() {
        return tags;
    }

    public void setTags(List<TagsForTopicOne> tags) {
        this.tags = tags;
    }
}
