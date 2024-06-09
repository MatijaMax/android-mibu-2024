package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrganizerProfilePageData implements Parcelable {
    private String email;
    public OrganizerProfilePageData(){}
    protected OrganizerProfilePageData(Parcel in){
        email = in.readString();
    }

    public static final Creator<OrganizerProfilePageData> CREATOR = new Creator<OrganizerProfilePageData>() {
        @Override
        public OrganizerProfilePageData createFromParcel(Parcel in) {
            return new OrganizerProfilePageData(in);
        }

        @Override
        public OrganizerProfilePageData[] newArray(int size) {
            return new OrganizerProfilePageData[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(email);
    }
    @Override
    public int describeContents() {
        return 0;
    }

}
