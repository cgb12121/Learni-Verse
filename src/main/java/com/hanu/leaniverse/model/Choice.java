package com.hanu.leaniverse.model;

import jakarta.persistence.*;

@Entity
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private int isTrue;
    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int isTrue() {
        return isTrue;
    }

    public void setTrue(int aTrue) {
        isTrue = aTrue;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return content;
    }
}
