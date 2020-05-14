package com.example.saathi.model;

import io.realm.RealmObject;

public class UserProfile extends RealmObject {

    private String fullName, dateOfBirth, phoneNumber, bloodGrp, illness, address, pin, city;

    public UserProfile() {
    }

    public UserProfile(String fullName, String dateOfBirth, String phoneNumber, String bloodGrp, String illness, String address, String pin, String city) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.bloodGrp = bloodGrp;
        this.illness = illness;
        this.address = address;
        this.pin = pin;
        this.city = city;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "fullName='" + fullName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bloodGrp='" + bloodGrp + '\'' +
                ", illness='" + illness + '\'' +
                ", address='" + address + '\'' +
                ", pin='" + pin + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
