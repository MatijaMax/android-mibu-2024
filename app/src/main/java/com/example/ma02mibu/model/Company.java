package com.example.ma02mibu.model;

import java.util.ArrayList;

public class Company {

    private String id;
    private String name;
    private ArrayList<Employee> employees;

    public Company(String name, ArrayList<Employee> employees) {
        this.name = name;
        this.employees = employees;
    }

    public Company(String id, String name, ArrayList<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public Company() {
    }

    public Company(String id, String name) {
        this.id = id;
        this.name = name;
        this.employees = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }
}
