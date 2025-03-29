package com.hanu.leaniverse.dto;

import java.util.List;

public class QuestionDTO {
    private int questionId;
    private boolean choice1;
    private boolean choice2;
    private boolean choice3;
    private boolean choice4;
    private boolean choice5;

    public QuestionDTO() {
    }

    public QuestionDTO(int questionId, boolean choice1, boolean choice2, boolean choice3, boolean choice4, boolean choice5) {
        this.questionId = questionId;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isChoice1() {
        return choice1;
    }

    public void setChoice1(boolean choice1) {
        this.choice1 = choice1;
    }

    public boolean isChoice2() {
        return choice2;
    }

    public void setChoice2(boolean choice2) {
        this.choice2 = choice2;
    }

    public boolean isChoice3() {
        return choice3;
    }

    public void setChoice3(boolean choice3) {
        this.choice3 = choice3;
    }

    public boolean isChoice4() {
        return choice4;
    }

    public void setChoice4(boolean choice4) {
        this.choice4 = choice4;
    }

    public boolean isChoice5() {
        return choice5;
    }

    public void setChoice5(boolean choice5) {
        this.choice5 = choice5;
    }
}
