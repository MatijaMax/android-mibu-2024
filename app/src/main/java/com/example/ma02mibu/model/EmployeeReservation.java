package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class EmployeeReservation implements Parcelable {
    private String employeeRefId;
    private String eventOrganizerRefId;
    private String serviceRefId;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean isPickedUp;
    private boolean isCanceled;

    public EmployeeReservation() { }

    public EmployeeReservation(String employeeRefId, String eventOrganizerRefId, String serviceRefId, LocalDateTime start, LocalDateTime end, boolean isPickedUp, boolean isCanceled) {
        this.employeeRefId = employeeRefId;
        this.eventOrganizerRefId = eventOrganizerRefId;
        this.serviceRefId = serviceRefId;
        this.start = start;
        this.end = end;
        this.isPickedUp = isPickedUp;
        this.isCanceled = isCanceled;
    }

    protected EmployeeReservation(Parcel in) {
        employeeRefId = in.readString();
        eventOrganizerRefId = in.readString();
        serviceRefId = in.readString();
        isPickedUp = in.readByte() != 0;
        isCanceled = in.readByte() != 0;
    }

    public static final Creator<EmployeeReservation> CREATOR = new Creator<EmployeeReservation>() {
        @Override
        public EmployeeReservation createFromParcel(Parcel in) {
            return new EmployeeReservation(in);
        }

        @Override
        public EmployeeReservation[] newArray(int size) {
            return new EmployeeReservation[size];
        }
    };

    public String getEmployeeRefId() {
        return employeeRefId;
    }

    public void setEmployeeRefId(String employeeRefId) {
        this.employeeRefId = employeeRefId;
    }

    public String getEventOrganizerRefId() {
        return eventOrganizerRefId;
    }

    public void setEventOrganizerRefId(String eventOrganizerRefId) {
        this.eventOrganizerRefId = eventOrganizerRefId;
    }

    public String getServiceRefId() {
        return serviceRefId;
    }

    public void setServiceRefId(String serviceRefId) {
        this.serviceRefId = serviceRefId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(employeeRefId);
        dest.writeString(eventOrganizerRefId);
        dest.writeString(serviceRefId);
        dest.writeByte((byte) (isPickedUp ? 1 : 0));
        dest.writeByte((byte) (isCanceled ? 1 : 0));
    }
}
