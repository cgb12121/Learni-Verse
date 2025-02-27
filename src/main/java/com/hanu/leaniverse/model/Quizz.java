package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;
@Entity
public class Quizz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quizzId;
    @ManyToOne
    @JoinColumn(name = "unitId")
    private Unit unit;
    @OneToMany(mappedBy = "quizz")
    private List<User_Quizz> user_quizzList;
    @OneToMany(mappedBy = "quizz")
    private List<Question> questions;
    @Column(nullable = false)
    private String quizzName;
    private Date createAt;


    public Quizz() {
    }

    public String getQuizzName() {
        return quizzName;
    }

    public void setQuizzName(String quizzName) {
        this.quizzName = quizzName;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(int quizzId) {
        this.quizzId = quizzId;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public List<User_Quizz> getUser_quizzList() {
        return user_quizzList;
    }

    public void setUser_quizzList(List<User_Quizz> user_quizzList) {
        this.user_quizzList = user_quizzList;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
