package ru.skillbox.diplom.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserResponseAuth {
    private Boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserFullResponse user;

    public UserResponseAuth(Boolean result) {
        this.result = result;
    }

    public UserResponseAuth(Boolean result, UserFullResponse user) {
        this.result = result;
        this.user = user;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public UserFullResponse getUser() {
        return user;
    }

    public void setUser(UserFullResponse user) {
        this.user = user;
    }
}
