package ru.skillbox.diplom.api.responses;
//TODO удалить это все, вынести в пропертис с сразу в контроллере это выводить
public class InitData {
    private String title;
    private String subtitle;
    private String phone;
    private String email;
    private String copyright;
    private String copyrightFrom;

    public InitData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }

    public void setCopyrightFrom(String copyrightFrom) {
        this.copyrightFrom = copyrightFrom;
    }

    public static InitData getInitData(){
        InitData initData = new InitData();
        initData.setTitle("title");
        initData.setSubtitle("subtitle");
        initData.setPhone("+7 913 712 xx xx");
        initData.setEmail("Q1122333@yandex.ru");
        initData.setCopyright("copyright");
        initData.setCopyrightFrom("2020");
        return initData;
    }
}
