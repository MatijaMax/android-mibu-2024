package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Owner implements Parcelable {
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String addressOfResidence;
    private String userUID;
    private Company myCompany;
    private ArrayList<String> categories;
    private ArrayList<String> eventTypes;
    private boolean blocked;
    private String documentRefId;
    public Owner() {
    }

    public Owner(String email, String name, String surname, String phoneNumber, String addressOfResidence, String userUID) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.addressOfResidence = addressOfResidence;
        this.userUID = userUID;
        this.myCompany = null;
        categories = new ArrayList<>();
        eventTypes = new ArrayList<>();
    }

    public Owner(String email, String name, String surname, String phoneNumber, String addressOfResidence, String userUID, Company myCompany) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.addressOfResidence = addressOfResidence;
        this.userUID = userUID;
        this.myCompany = myCompany;
        categories = new ArrayList<>();
        eventTypes = new ArrayList<>();
    }

    protected Owner(Parcel in) {
        documentRefId = in.readString();
        email = in.readString();
        name = in.readString();
        surname = in.readString();
        phoneNumber = in.readString();
        addressOfResidence = in.readString();
        userUID = in.readString();
        myCompany = in.readParcelable(Company.class.getClassLoader());
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(documentRefId);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(phoneNumber);
        dest.writeString(addressOfResidence);
        dest.writeString(userUID);
        dest.writeParcelable(myCompany, flags);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressOfResidence() {
        return addressOfResidence;
    }

    public void setAddressOfResidence(String addressOfResidence) {
        this.addressOfResidence = addressOfResidence;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Company getMyCompany() {
        return myCompany;
    }

    public void setMyCompany(Company myCompany) {
        this.myCompany = myCompany;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(ArrayList<String> eventTypes) {
        this.eventTypes = eventTypes;
    }
    @Exclude
    public String getDocumentRefId() {
        return documentRefId;
    }

    public void setDocumentRefId(String documentRefId) {
        this.documentRefId = documentRefId;
    }
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
