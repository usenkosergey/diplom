package ru.skillbox.diplom.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserResponse {

    private int id;
    private String name;
    private String photo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean moderation;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int moderationCount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean settings;

    public UserResponse() {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isModeration() {
        return moderation;
    }

    public void setModeration(boolean moderation) {
        this.moderation = moderation;
    }

    public int getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(int moderationCount) {
        this.moderationCount = moderationCount;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }
}
