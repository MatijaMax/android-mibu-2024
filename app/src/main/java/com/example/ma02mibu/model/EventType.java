package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EventType implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private EVENTTYPESTATUS status;
    public enum EVENTTYPESTATUS  {ACTIVE, DEACTIVATED};

    public EventType() {
    }

    public EventType(Long id, String name, String description, EVENTTYPESTATUS status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    protected EventType(Parcel in){
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        status = EVENTTYPESTATUS.values()[in.readInt()];
    }

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

    public EVENTTYPESTATUS getStatus() {
        return status;
    }

    public void setStatus(EVENTTYPESTATUS status) {
        this.status = status;
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
        dest.writeInt(status.ordinal());
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
}
