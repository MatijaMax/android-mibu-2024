package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Event implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private int participantNumber;
    private String location;
    private Boolean privacy;
    private String date;


    protected Event(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        participantNumber = in.readInt();
        location = in.readString();
        privacy = in.readBoolean();
        date = in.readString();
    }

    public Event(Long id, String name, String description, int participantNumber, String location, Boolean privacy, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.participantNumber = participantNumber;
        this.location = location;
        this.privacy = privacy;
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(participantNumber);
        dest.writeString(location);
        dest.writeBoolean(privacy);
        dest.writeString(date);
    }
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getParticipantNumber() {
        return participantNumber;
    }

    public void setParticipantNumber(int participantNumber) {
        this.participantNumber = participantNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

