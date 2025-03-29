package com.hanu.leaniverse.service;

import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.Review;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.repository.CategoryRepository;
import com.hanu.leaniverse.repository.CourseRepository;
import com.hanu.leaniverse.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review addReview(ReviewDTO reviewDTO, User user) {
        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        review.setStar(reviewDTO.getStar());
        review.setUser(user);

        Course course = courseRepository.findById(reviewDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        review.setCourse(course);

        return reviewRepository.save(review);
    }
}
