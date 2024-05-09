package com.example.ma02mibu.model;

public class OurNotification {

    private String email;
    private String title;
    private String text;
    private String status;

    public OurNotification(String email, String title, String text, String status) {
        this.email = email;
        this.title = title;
        this.text = text;
        this.status = status;
    }

    public OurNotification(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
