package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Integer> {
    List<CourseCategory> findByCourse(Course course);
}
