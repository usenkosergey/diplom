package ru.skillbox.diplom.api.reguests;

public class Register {
    private String email;
    private String password;
    private String captcha;
    private String captcha_secret;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
