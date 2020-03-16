package ru.skillbox.diplom.api.requests;

public class PasswordRequest {
    private String code;
    private String password;
    private String captcha;
    private String captcha_secret;

    public PasswordRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptcha_secret() {
        return captcha_secret;
    }

    public void setCaptcha_secret(String captcha_secret) {
        this.captcha_secret = captcha_secret;
    }
}
