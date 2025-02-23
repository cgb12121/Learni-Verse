package com.hanu.leaniverse.model;

import jakarta.persistence.*;
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;
    private String requirement;
    private String choosen1;
    private String choosen2;
    private String choosen3;
    private String choosen4;
    private String answer;
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

    public String getChoosen1() {
        return choosen1;
    }

    public void setChoosen1(String choosen1) {
        this.choosen1 = choosen1;
    }

    public String getChoosen2() {
        return choosen2;
    }

    public void setChoosen2(String choosen2) {
        this.choosen2 = choosen2;
    }

    public String getChoosen3() {
        return choosen3;
    }

    public void setChoosen3(String choosen3) {
        this.choosen3 = choosen3;
    }

    public String getChoosen4() {
        return choosen4;
    }

    public void setChoosen4(String choosen4) {
        this.choosen4 = choosen4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }
}
