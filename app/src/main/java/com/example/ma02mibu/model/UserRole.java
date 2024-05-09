package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserRole implements Parcelable {
    private String userEmail;
    private USERROLE userRole;

    protected UserRole(Parcel in) {
        userEmail = in.readString();
    }

    public static final Creator<UserRole> CREATOR = new Creator<UserRole>() {
        @Override
        public UserRole createFromParcel(Parcel in) {
            return new UserRole(in);
        }

        @Override
        public UserRole[] newArray(int size) {
            return new UserRole[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userEmail);
    }

    public enum USERROLE {ADMIN, OWNER, EMPLOYEE, ORGANIZER}

    public UserRole() { }

    public UserRole(String userEmail, USERROLE userRole) {
        this.userEmail = userEmail;
        this.userRole = userRole;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public USERROLE getUserRole() {
        return userRole;
    }

    public void setUserRole(USERROLE userRole) {
        this.userRole = userRole;
    }
}
