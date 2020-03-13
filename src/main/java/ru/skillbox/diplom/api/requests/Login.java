package ru.skillbox.diplom.api.requests;

public class Login {
    private String e_mail;
    private String password;

    public Login() {
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
