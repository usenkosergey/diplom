package ru.skillbox.diplom.api.responses;

import java.util.List;

public class TagsForTopicResponse {
    List<TagForTopicOne> tags;

    public TagsForTopicResponse() {
    }

    public List<TagForTopicOne> getTags() {
        return tags;
    }

    public void setTags(List<TagForTopicOne> tags) {
        this.tags = tags;
    }
}
