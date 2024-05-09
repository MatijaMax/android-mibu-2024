package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Category implements Parcelable {
    private String documentRefId;
    private String name;
    private String description;

    public Category() { }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Category(Parcel in){
        documentRefId = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    public String getDocumentRefId() {
        return documentRefId;
    }

    public void setDocumentRefId(String documentRefId) {
        this.documentRefId = documentRefId;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(documentRefId);
        dest.writeString(name);
        dest.writeString(description);
    }
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
