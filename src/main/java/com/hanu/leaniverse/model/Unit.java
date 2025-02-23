package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int unitId;
    private String Description;
    @OneToMany(mappedBy = "unit")
    private List<Video> video;
    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;
    @OneToMany(mappedBy = "unit")
    private List<Quizz> quizzes;

    public Unit() {
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Quizz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quizz> quizzes) {
        this.quizzes = quizzes;
    }
}
