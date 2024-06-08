package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Date;

public class EmployeeReservation implements Parcelable {
    private String employeeEmail;
    private String eventOrganizerRefId;
    private String serviceRefId;
    private Date start;
    private Date end;
    private String packageRefId;
    private ReservationStatus status;
    public enum ReservationStatus {New, CanceledByPUP, CanceledByOD, CanceledByAdmin ,Accepted, Finished};

    public EmployeeReservation() { }

    public EmployeeReservation(String employeeEmail, String eventOrganizerRefId, String serviceRefId, Date start, Date end, String packageRefId, ReservationStatus status) {
        this.employeeEmail = employeeEmail;
        this.eventOrganizerRefId = eventOrganizerRefId;
        this.serviceRefId = serviceRefId;
        this.start = start;
        this.end = end;
        this.packageRefId = packageRefId;
        this.status = status;
    }

    protected EmployeeReservation(Parcel in) {
        employeeEmail = in.readString();
        eventOrganizerRefId = in.readString();
        serviceRefId = in.readString();
        packageRefId = in.readString();
        start = new Date(in.readLong());
        end = new Date(in.readLong());
        status = ReservationStatus.values()[in.readInt()];
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPackageRefId() {
        return packageRefId;
    }

    public void setPackageRefId(String packageRefId) {
        this.packageRefId = packageRefId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(employeeEmail);
        dest.writeString(eventOrganizerRefId);
        dest.writeString(serviceRefId);
        dest.writeString(packageRefId);
        dest.writeLong(start.getTime());
        dest.writeLong(end.getTime());
        dest.writeInt(status.ordinal());
    }
}
