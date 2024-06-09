package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProfilePageData implements Parcelable {
    private String userId;
    private int role;
    public ProfilePageData(){}
    protected ProfilePageData(Parcel in){
        userId = in.readString();
        role = in.readInt();
    }

    public static final Creator<ProfilePageData> CREATOR = new Creator<ProfilePageData>() {
        @Override
        public ProfilePageData createFromParcel(Parcel in) {
            return new ProfilePageData(in);
        }

        @Override
        public ProfilePageData[] newArray(int size) {
            return new ProfilePageData[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeInt(role);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
