package com.hanu.leaniverse.service;

import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.CategoryRepository;
import com.hanu.leaniverse.repository.CourseRepository;
import com.hanu.leaniverse.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    public Map<String, Object> getHomePageData(String title, Integer categoryId) {
        Map<String, Object> data = new HashMap<>();

        List<Course> courses;
        if (title != null && !title.isEmpty() && categoryId != null) {
            // Kết hợp cả search và filter
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                courses = courseRepository.findByTitleContainingAndCategory(title, category);
            } else {
                courses = courseRepository.findByTitleContaining(title);
            }
        } else if (title != null && !title.isEmpty()) {
            // Chỉ search theo title
            courses = courseRepository.findByTitleContaining(title);
        } else if (categoryId != null) {
            // Chỉ filter theo category
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                courses = courseRepository.findByCategory(category);
            } else {
                courses = courseRepository.findAll();
            }
        } else {
            // Nếu không có điều kiện nào, trả về tất cả khóa học
            courses = courseRepository.findAll();
        }

        // rating
        Map<Integer, Double> courseRatings = new HashMap<>();
        for (Course course : courses) {
            double averageRating = calculateAverageRating(course);
            courseRatings.put(course.getCourseId(), averageRating);
        }

        List<Category> categories = categoryRepository.findAll();

        data.put("courses", courses);
        data.put("categories", categories);
        data.put("courseRatings", courseRatings);
        return data;
    }

    @Override
    public Map<String, Object> getCourseDetailData(int courseId) {
        Map<String, Object> data = new HashMap<>();

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        Tutor tutor = null;
        if (!course.getTeaches().isEmpty()) {
            tutor = course.getTeaches().get(0).getTutor();
        }

        List<Review> reviews = course.getReviews();

        // calculate rating
        double averageRating = calculateAverageRating(course);

        // related course list
        List<Course> relatedCourses = new ArrayList<>();
        if (!course.getCourseCategories().isEmpty()) {
            int categoryId = course.getCourseCategories().get(0).getCategory().getCategoryId();
            relatedCourses = courseRepository.findRelatedCourses(categoryId, courseId);
        }

        data.put("course", course);
        data.put("tutor", tutor);
        data.put("reviews", reviews);
        data.put("averageRating", averageRating);
        data.put("relatedCourses", relatedCourses);

        return data;
    }

    private double calculateAverageRating(Course course) {
        return course.getReviews().stream()
                .mapToInt(Review::getStar)
                .average()
                .orElse(0.0);
    }

}
