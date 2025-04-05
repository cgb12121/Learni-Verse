package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.awt.*;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;
    private String requirement;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;//dung break de pha vong for
    private boolean isChoice1;
    private boolean isChoice2;
    private boolean isChoice3;
    private boolean isChoice4;
    private boolean isChoice5;
    @ManyToOne
    @JoinColumn(name = "quizzId")
    private Quizz quizz;

    public Question() {
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public String getChoice5() {
        return choice5;
    }

    public void setChoice5(String choice5) {
        this.choice5 = choice5;
    }

    public boolean getIsChoice1() {
        return isChoice1;
    }

    public void setIsChoice1(boolean choice1) {
        isChoice1 = choice1;
    }

    public boolean getIsChoice2() {
        return isChoice2;
    }

    public void setIsChoice2(boolean choice2) {
        isChoice2 = choice2;
    }

    public boolean getIsChoice3() {
        return isChoice3;
    }

    public void setIsChoice3(boolean choice3) {
        isChoice3 = choice3;
    }

    public boolean getIsChoice4() {
        return isChoice4;
    }

    public void setIsChoice4(boolean choice4) {
        isChoice4 = choice4;
    }

    public boolean getIsChoice5() {
        return isChoice5;
    }

    public void setIsChoice5(boolean choice5) {
        isChoice5 = choice5;
    }

    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }
}
