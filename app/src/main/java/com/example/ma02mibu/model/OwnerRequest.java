package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OwnerRequest implements Parcelable {
    private String documentRefId;
    private Owner owner;
    private String password;
    private String created;

    public OwnerRequest() {
    }

    public OwnerRequest(Owner owner, String password) {
        this.owner = owner;
        this.password = password;
        this.created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).toString();
    }

    protected OwnerRequest(Parcel in) {
        owner = in.readParcelable(Owner.class.getClassLoader());
        password = in.readString();
    }

    public static final Creator<OwnerRequest> CREATOR = new Creator<OwnerRequest>() {
        @Override
        public OwnerRequest createFromParcel(Parcel in) {
            return new OwnerRequest(in);
        }

        @Override
        public OwnerRequest[] newArray(int size) {
            return new OwnerRequest[size];
        }
    };

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getDocumentRefId() {
        return documentRefId;
    }

    public void setDocumentRefId(String documentRefId) {
        this.documentRefId = documentRefId;
    }

    public String getCreated() {
        return created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(owner, flags);
        dest.writeString(password);
    }
}
