package com.example.ma02mibu.model;

public class OurNotification {

    private String userUID;
    private String title;
    private String text;
    private String status;

    public OurNotification(String userUID, String title, String text, String status) {
        this.userUID = userUID;
        this.title = title;
        this.text = text;
        this.status = status;
    }

    public OurNotification(){}

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
