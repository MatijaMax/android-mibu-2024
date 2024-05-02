package com.example.ma02mibu.model;

public class Owner {
    private String id;

    private  String firstName;
    private Company myCompany;

    public Owner() {
    }

    public Owner(String firstName, Company myCompany) {
        this.firstName = firstName;
        this.myCompany = myCompany;
    }

    public Owner(String id, String firstName, Company myCompany) {
        this.id = id;
        this.firstName = firstName;
        this.myCompany = myCompany;
    }

    public Owner(String id, String firstName) {
        this.id = id;
        this.firstName = firstName;
        this.myCompany = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Company getMyCompany() {
        return myCompany;
    }

    public void setMyCompany(Company myCompany) {
        this.myCompany = myCompany;
    }
}
