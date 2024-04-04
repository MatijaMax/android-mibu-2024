package com.example.ma02mibu.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventModel {
    private String name;
    private LocalDate date;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String status;

    public EventModel(){}

    public EventModel(String name, LocalDate date, LocalTime fromTime, LocalTime toTime, String status) {
        this.name = name;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
