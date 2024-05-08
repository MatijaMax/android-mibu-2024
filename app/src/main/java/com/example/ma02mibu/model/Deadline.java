package com.example.ma02mibu.model;

public class Deadline {
    private String dateFormat;
    private int number;

    public Deadline(){}
    public Deadline(String dateFormat, int number) {
        this.dateFormat = dateFormat;
        this.number = number;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
