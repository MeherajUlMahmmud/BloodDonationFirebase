package com.example.blooddonationfirebase.models;

public class Request {
    private String id;
    private String userId;
    private String patientName;
    private String gender;
    private String bloodGroup;
    private String location;
    private String neededWithin;
    private String unit;
    private String phone;
    private String note;
    private String postedOn;

    public Request() {
    }

    public Request(String id, String userId, String patientName, String gender, String bloodGroup, String location, String neededWithin, String unit, String phone, String note, String postedOn) {
        this.id = id;
        this.userId = userId;
        this.patientName = patientName;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.neededWithin = neededWithin;
        this.unit = unit;
        this.phone = phone;
        this.note = note;
        this.postedOn = postedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNeededWithin() {
        return neededWithin;
    }

    public void setNeededWithin(String neededWithin) {
        this.neededWithin = neededWithin;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }
}
