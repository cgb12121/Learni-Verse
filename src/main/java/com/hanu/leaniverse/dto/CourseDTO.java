package com.hanu.leaniverse.dto;

import org.springframework.web.multipart.MultipartFile;

public class CourseDTO {
    private int courseId;
    private String courseName;
    private String courseDetail;
    private double price;
    private MultipartFile courseImage;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(MultipartFile courseImage) {
        this.courseImage = courseImage;
    }
}
