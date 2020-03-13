package ru.skillbox.diplom.api.responses;

public class UserResponseAuth {
    private Boolean result;
    private UserResponse user;

    public UserResponseAuth(Boolean result) {
        this.result = result;
    }

    public UserResponseAuth(Boolean result, UserResponse user) {
        this.result = result;
        this.user = user;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
