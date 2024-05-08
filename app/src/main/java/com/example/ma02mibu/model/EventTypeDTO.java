package com.example.ma02mibu.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EventTypeDTO implements Parcelable {

    private String name;
    private String description;
    private String subcategories;


    public EventTypeDTO() {
    }

    public EventTypeDTO(String name, String description, String subcategories) {

        this.name = name;
        this.description = description;
        this.subcategories = subcategories;
    }

    protected EventTypeDTO(Parcel in){

        name = in.readString();
        description = in.readString();
        subcategories = in.readString();
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(subcategories);
    }

    public static final Creator<EventType> CREATOR = new Creator<EventType>() {
        @Override
        public EventType createFromParcel(Parcel in) {
            return new EventType(in);
        }

        @Override
        public EventType[] newArray(int size) {
            return new EventType[size];
        }
    };

    public String getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(String subcategories) {
        this.subcategories = subcategories;
    }
}

