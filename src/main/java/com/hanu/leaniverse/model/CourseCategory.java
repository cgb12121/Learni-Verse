package com.hanu.leaniverse.model;

import jakarta.persistence.*;
@Entity
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseCateId;
    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public CourseCategory() {
    }

    public int getCourseCateId() {
        return courseCateId;
    }

    public void setCourseCateId(int courseCateId) {
        this.courseCateId = courseCateId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
