package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.model.Course;

import java.util.Map;

public interface CourseService{

    Map<String, Object> getHomePageData(String title, Integer categoryId);

    Map<String, Object> getCourseDetailData(int courseId);

    Course getCourseById(int courseId);

}
