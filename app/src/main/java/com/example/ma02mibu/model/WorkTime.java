package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalTime;

public class WorkTime implements Parcelable {
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Parcelable methods
    protected WorkTime(Parcel in) {
        // Read data from the parcel and initialize your fields
        startTime = LocalTime.parse(in.readString());
        endTime = LocalTime.parse(in.readString());
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

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}
