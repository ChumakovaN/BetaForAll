package com.example.betaforall.model;
public class Employee {
    public String id;
    public String fullName;
    public String brigade;

    public Employee() {
    }

    public Employee(String id, String fullName, String brigade) {
        this.id = id;
        this.fullName = fullName;
        this.brigade = brigade;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }
}
