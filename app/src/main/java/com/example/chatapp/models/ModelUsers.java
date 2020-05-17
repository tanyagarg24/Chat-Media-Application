package com.example.chatapp.models;

public class ModelUsers {
    String name,image,about,uid;

    public ModelUsers() {
    }

    public ModelUsers(String name, String image, String about, String uid) {
        this.name = name;
        this.image = image;
        this.about = about;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
