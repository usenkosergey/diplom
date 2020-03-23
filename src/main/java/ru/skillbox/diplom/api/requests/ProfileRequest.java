package ru.skillbox.diplom.api.requests;

public class ProfileRequest {
    private String photo;
    private int removePhoto;
    private String name;
    private String email;
    private String password;

    public ProfileRequest() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(int removePhoto) {
        this.removePhoto = removePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
