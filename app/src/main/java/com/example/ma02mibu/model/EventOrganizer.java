package com.example.ma02mibu.model;

public class EventOrganizer {
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String addressOfResidence;
    private String userUID;

    public EventOrganizer(){}

    public EventOrganizer(String email, String name, String surname, String phoneNumber, String addressOfResidence, String userUID) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.addressOfResidence = addressOfResidence;
        this.userUID = userUID;
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
}
