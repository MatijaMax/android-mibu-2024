package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Employee implements Parcelable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private int image;

    private String ownerRefId;
    private String userUID;
    private ArrayList<WorkSchedule> workSchedules;

    private int isActive;

    public Employee() {
        workSchedules = new ArrayList<>();
    }

    public Employee(Long id, String firstName, String lastName, String email, String password, String address, String phoneNumber, int image, String ownerRefId, int isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.ownerRefId = ownerRefId;
        this.isActive = isActive;
        workSchedules = new ArrayList<>();
    }

    public Employee(Long id, String firstName, String lastName, String email, String password, String address, String phoneNumber, int image, String ownerRefId, String userUID, ArrayList<WorkSchedule> workSchedules, int isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.ownerRefId = ownerRefId;
        this.userUID = userUID;
        this.workSchedules = workSchedules;
        this.isActive = isActive;
    }

    protected Employee(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        image = in.readInt();
        ownerRefId = in.readString();
        userUID = in.readString();
        workSchedules = in.createTypedArrayList(WorkSchedule.CREATOR);
    }


    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(phoneNumber);
        parcel.writeInt(image);
        parcel.writeString(ownerRefId);
        parcel.writeString(userUID);
        parcel.writeTypedList(workSchedules);
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public String getOwnerRefId() {
        return ownerRefId;
    }

    public void setOwnerRefId(String ownerRefId) {
        this.ownerRefId = ownerRefId;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public WorkSchedule findActiveWorkSchedule() {
        return workSchedules.get(0);
    }

    public WorkSchedule findActiveWorkScheduleAlt(LocalDate d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(WorkSchedule ws : workSchedules){
            if(ws.getStartDay() != null && ws.getEndDay() != null){
                LocalDate ds = LocalDate.parse(ws.getStartDay(), formatter);
                LocalDate df = LocalDate.parse(ws.getEndDay(), formatter);
                if(!d.isBefore(ds) && !d.isAfter(df)){
                    return ws;
                }
            }
        }
        return workSchedules.get(0);
    }

    public ArrayList<WorkSchedule> getWorkSchedules() {
        return workSchedules;
    }

    public void setWorkSchedules(ArrayList<WorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
    }

    public void setSchedule(WorkSchedule workSchedule) {
        this.workSchedules.add(workSchedule);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
