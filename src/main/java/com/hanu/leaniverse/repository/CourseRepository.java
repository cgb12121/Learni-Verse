package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Integer> {
    
}
