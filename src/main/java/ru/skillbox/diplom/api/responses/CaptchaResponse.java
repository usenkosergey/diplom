package ru.skillbox.diplom.api.responses;

public class CaptchaResponse {

    private String secret;
    private String image;

    public CaptchaResponse() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
