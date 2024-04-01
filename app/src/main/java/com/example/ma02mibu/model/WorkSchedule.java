package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WorkSchedule implements Parcelable {
    private Map<DayOfWeek, WorkTime> schedule;

    public WorkSchedule() {
        schedule = new HashMap<>();
    }

    // Parcelable methods
    protected WorkSchedule(Parcel in) {
        // Read data from the parcel and initialize your fields
        schedule = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(in.readString());
            WorkTime workTime = in.readParcelable(WorkTime.class.getClassLoader());
            schedule.put(dayOfWeek, workTime);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data to the parcel
        dest.writeInt(schedule.size());
        for (Map.Entry<DayOfWeek, WorkTime> entry : schedule.entrySet()) {
            dest.writeString(entry.getKey().name());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkSchedule> CREATOR = new Creator<WorkSchedule>() {
        @Override
        public WorkSchedule createFromParcel(Parcel in) {
            return new WorkSchedule(in);
        }

        @Override
        public WorkSchedule[] newArray(int size) {
            return new WorkSchedule[size];
        }
    };
    public Map<DayOfWeek, WorkTime> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<DayOfWeek, WorkTime> schedule) {
        this.schedule = schedule;
    }

    public void setWorkTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        schedule.put(dayOfWeek, new WorkTime(startTime, endTime));
    }
    public WorkTime getWorkTime(DayOfWeek dayOfWeek) {
        return schedule.get(dayOfWeek);
    }
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (DayOfWeek d : DayOfWeek.values()) {
            output.append(d.toString()).append(" => ").append(schedule.get(d).toString()).append("\n");
        }
        return output.toString();
    }
}
