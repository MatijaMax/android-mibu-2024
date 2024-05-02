package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WorkSchedule implements Parcelable {
    private Map<String, WorkTime> schedule;
    private String startDay;
    private String endDay;


    public WorkSchedule() {
        schedule = new HashMap<>();
    }

    // Parcelable methods
    protected WorkSchedule(Parcel in) {
        // Read data from the parcel and initialize your fields
        startDay = (String) in.readSerializable();
        endDay = (String) in.readSerializable();
        schedule = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(in.readString());
            WorkTime workTime = in.readParcelable(WorkTime.class.getClassLoader());
            schedule.put(dayOfWeek.toString(), workTime);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data to the parcel
        dest.writeSerializable(startDay);
        dest.writeSerializable(endDay);
        dest.writeInt(schedule.size());
        for (Map.Entry<String, WorkTime> entry : schedule.entrySet()) {
            dest.writeString(entry.getKey());
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
    public Map<String, WorkTime> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, WorkTime> schedule) {
        this.schedule = schedule;
    }

    public void setWorkTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if(startTime == null || endTime == null){
            schedule.put(dayOfWeek.toString(), null);
        }
        else {
            schedule.put(dayOfWeek.toString(), new WorkTime(startTime.toString(), endTime.toString()));
        }
    }
    public WorkTime getWorkTime(String dayOfWeek) {
        return schedule.get(dayOfWeek);
    }
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(startDay.toString()).append(" - ").append(endDay.toString()).append("\n");
        for (DayOfWeek d : DayOfWeek.values()) {
            output.append(d.toString()).append(" => ");
            if(schedule.get(d.toString()) == null){
                output.append("not working").append("\n");
            }
            else {
                output.append(schedule.get(d.toString()).toString()).append("\n");
            }
        }
        return output.toString();
    }

    public String ScheduleForDay(DayOfWeek d){
        if(schedule.get(d.toString()) == null){
            return "not working";
        }
        return schedule.get(d.toString()).toString();
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
}
