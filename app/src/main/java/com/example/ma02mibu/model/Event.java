package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event implements Parcelable {
    private Long id;

    private String type;
    private String name;
    private String description;
    private int participantNumber;
    private String location;
    private Boolean privacy;
    private String date;

    private String email;

    private List<Guest> guests;

    private List<AgendaItem> agenda;



    protected Event(Parcel in) {
        id = in.readLong();
        type = in.readString();
        name = in.readString();
        description = in.readString();
        participantNumber = in.readInt();
        location = in.readString();
        privacy = in.readBoolean();
        date = in.readString();
        guests = new ArrayList<>();
        in.readList(guests, Guest.class.getClassLoader());
        agenda = new ArrayList<>();
        in.readList(agenda, AgendaItem.class.getClassLoader());
    }

    public Event(Long id, String type, String name, String description, int participantNumber, String location, Boolean privacy, String date, String email) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.participantNumber = participantNumber;
        this.location = location;
        this.privacy = privacy;
        this.date = date;
        this.type = type;
        this.email = email;
    }

    public Event(Long id, String type, String name, String description, int participantNumber, String location, Boolean privacy, String date, String email, List<Guest> guests, List<AgendaItem> agendaItems ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.participantNumber = participantNumber;
        this.location = location;
        this.privacy = privacy;
        this.date = date;
        this.type = type;
        this.email = email;
        this.guests = guests;
        this.agenda = agendaItems;
    }

    public Event(){

    }


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(participantNumber);
        dest.writeString(location);
        dest.writeBoolean(privacy);
        dest.writeString(date);
        dest.writeList(guests);
        dest.writeList(agenda);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public List<AgendaItem> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<AgendaItem> agendaItems) {
        this.agenda = agendaItems;
    }
}

