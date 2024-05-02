package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalTime;

public class WorkTime implements Parcelable {
    private String startTime;
    private String endTime;

    public WorkTime() {
    }

    public WorkTime(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Parcelable methods
    protected WorkTime(Parcel in) {
        // Read data from the parcel and initialize your fields
        startTime = in.readString();
        endTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data to the parcel
        dest.writeString(startTime.toString());
        dest.writeString(endTime.toString());
    }

    public static final Creator<WorkTime> CREATOR = new Creator<WorkTime>() {
        @Override
        public WorkTime createFromParcel(Parcel in) {
            return new WorkTime(in);
        }

        @Override
        public WorkTime[] newArray(int size) {
            return new WorkTime[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}
