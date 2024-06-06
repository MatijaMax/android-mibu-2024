package com.example.ma02mibu.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Guest{


    public Guest(){

    }

    public Guest(String name, int age, Boolean isInvited, Boolean hasAccepted, String special){
        this.age = age;
        this.name = name;
        this.isInvited = isInvited;
        this.hasAccepted = hasAccepted;
        this.special = special;
    }

    private String name;
    private int age;
    private Boolean isInvited;

    private Boolean hasAccepted;

    private String special;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getInvited() {
        return isInvited;
    }

    public void setInvited(Boolean invited) {
        isInvited = invited;
    }

    public Boolean getHasAccepted() {
        return hasAccepted;
    }

    public void setHasAccepted(Boolean hasAccepted) {
        this.hasAccepted = hasAccepted;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


