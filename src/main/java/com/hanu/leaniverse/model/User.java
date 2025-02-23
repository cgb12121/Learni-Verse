package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String fullName;
    private String password;
    @OneToOne(mappedBy = "user")
    private Tutor tutor;
    @OneToMany(mappedBy = "user")
    private List<Favourite> favourite;
    @OneToMany(mappedBy = "user")
    private List<WishList> wishLists;
    @OneToOne(mappedBy = "user")
    private UserSensitiveInformation userSensitiveInformation;
    @OneToMany(mappedBy = "user")
    private List<PaymentInformation> paymentInformation;
    @OneToMany(mappedBy = "user")
    private List<Report> reports;
    @OneToMany(mappedBy = "user")
    private List<Certificate> certificates;
    @OneToMany(mappedBy = "user")
    private List<Enrollment> enrollments;
    @OneToMany(mappedBy = "user")
    private List<Cart> carts;
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
    @OneToMany(mappedBy = "user")
    private List<User_Quizz> user_quizzes;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public List<Favourite> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<Favourite> favourite) {
        this.favourite = favourite;
    }

    public List<WishList> getWishLists() {
        return wishLists;
    }

    public void setWishLists(List<WishList> wishLists) {
        this.wishLists = wishLists;
    }

    public UserSensitiveInformation getUserSensitiveInformation() {
        return userSensitiveInformation;
    }

    public void setUserSensitiveInformation(UserSensitiveInformation userSensitiveInformation) {
        this.userSensitiveInformation = userSensitiveInformation;
    }

    public List<PaymentInformation> getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(List<PaymentInformation> paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<User_Quizz> getUser_quizzes() {
        return user_quizzes;
    }

    public void setUser_quizzes(List<User_Quizz> user_quizzes) {
        this.user_quizzes = user_quizzes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}