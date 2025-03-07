package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int favouriteId;
    private Date createAt;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = true)
    private Course course;

    public Favourite() {
    }

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
