package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tutorId;
    private String tutorDescribtion;
    private String contact;
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToMany(mappedBy = "tutor")
    private List<Teach> teaches;
    public Tutor() {
    }

    public List<Teach> getTeaches() {
        return teaches;
    }

    public void setTeaches(List<Teach> teaches) {
        this.teaches = teaches;
    }

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public String getTutorDescribtion() {
        return tutorDescribtion;
    }

    public void setTutorDescribtion(String tutorDescribtion) {
        this.tutorDescribtion = tutorDescribtion;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
