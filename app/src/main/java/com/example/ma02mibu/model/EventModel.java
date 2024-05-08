package com.example.ma02mibu.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventModel {
    private String name;
    private String date;
    private String fromTime;
    private String toTime;
    private String status;
    private String email;

    public EventModel(){}

    public EventModel(String name, String date, String fromTime, String toTime, String status, String email) {
        this.name = name;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.status = status;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
