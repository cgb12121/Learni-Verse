package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Teach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teachId;
    @ManyToOne
    @JoinColumn(name = "tutorId")
    private Tutor tutor;
    @ManyToOne
    @JoinColumn(name ="courseId")
    private Course course;
    private Date teachTime;

    public Teach() {
    }

    public int getTeachId() {
        return teachId;
    }

    public void setTeachId(int teachId) {
        this.teachId = teachId;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getTeachTime() {
        return teachTime;
    }

    public void setTeachTime(Date teachTime) {
        this.teachTime = teachTime;
    }
}
