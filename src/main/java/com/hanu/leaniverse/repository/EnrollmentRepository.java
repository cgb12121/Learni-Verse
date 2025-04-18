package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.Enrollment;
import com.hanu.leaniverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("SELECT DISTINCT e FROM Enrollment e WHERE e.user = :user")
    List<Enrollment> findByUser(@Param("user") User user);

    List<Enrollment> findByCourse(Course course);

    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.user.userId = :userId AND e.course.courseId = :courseId")
    boolean isUserEnrolled(@Param("userId") int userId, @Param("courseId") int courseId);
}
