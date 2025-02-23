package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.sql.Date;
@Entity
public class UserSensitiveInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userSenInfoId;
    private String email;
    private Date dob;
    private String pob;
    private String phoneNumber;
    private String address;
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public UserSensitiveInformation() {
    }

    public int getUserSenInfoId() {
        return userSenInfoId;
    }

    public void setUserSenInfoId(int userSenInfoId) {
        this.userSenInfoId = userSenInfoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPob() {
        return pob;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
