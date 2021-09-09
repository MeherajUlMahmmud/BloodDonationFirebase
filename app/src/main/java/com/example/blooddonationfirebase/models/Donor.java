package com.example.blooddonationfirebase.models;

public class Donor {
    private String id;
    private String name;
    private String gender;
    private String bloodGroup;
    private String phone;
    private String location;
    private String lastDonation;
    private String available;

    public Donor() {
    }

    public Donor(String id, String name, String gender, String bloodGroup, String phone, String location, String lastDonation, String available) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.location = location;
        this.lastDonation = lastDonation;
        this.available = available;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastDonation() {
        return lastDonation;
    }

    public void setLastDonation(String lastDonation) {
        this.lastDonation = lastDonation;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
