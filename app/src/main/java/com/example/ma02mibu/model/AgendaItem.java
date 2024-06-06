package com.example.ma02mibu.model;

public class AgendaItem {
    private String name;
    private String description;

    private String timespan;
    private String location;

    public AgendaItem(){

    }

    public AgendaItem(String name, String description, String timespan, String location) {
        this.name = name;
        this.description = description;
        this.timespan = timespan;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimespan() {
        return timespan;
    }

    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
