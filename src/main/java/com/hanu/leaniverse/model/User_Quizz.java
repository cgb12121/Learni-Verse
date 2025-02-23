package com.hanu.leaniverse.model;

import jakarta.persistence.*;
@Entity
public class User_Quizz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userQuizzId;
    private int grade;
    @ManyToOne
    @JoinColumn(name = "quizzId")
    private Quizz quizz;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public User_Quizz() {
    }

    public int getUserQuizzId() {
        return userQuizzId;
    }

    public void setUserQuizzId(int userQuizzId) {
        this.userQuizzId = userQuizzId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
