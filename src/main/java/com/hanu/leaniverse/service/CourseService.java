package com.hanu.leaniverse.service;

import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.model.Review;
import com.hanu.leaniverse.model.User;

import java.util.Map;

public interface CourseService{

    Map<String, Object> getHomePageData(String title, Integer categoryId);

    Map<String, Object> getCourseDetailData(int courseId);

}
