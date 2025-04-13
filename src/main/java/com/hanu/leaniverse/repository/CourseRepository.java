package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Category;
import com.hanu.leaniverse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c JOIN c.courseCategories cc WHERE cc.category = :category")
    List<Course> findByCategory(Category category);

    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %:title%")
    List<Course> findByTitleContaining(String title);

    @Query("SELECT c FROM Course c JOIN c.courseCategories cc WHERE c.courseName LIKE %:title% AND cc.category = :category")
    List<Course> findByTitleContainingAndCategory(@Param("title") String title, @Param("category") Category category);

    /// Trong JPQL không thể sử dụng LIMIT trực tiếp như trong SQL
    // --> Sử dụng nativeQuery để viết truy vấn SQL thuần túy và sử dụng LIMIT.
    // --> Phải sử dụng tên table giống trong MySQL
    @Query(value = "SELECT c.course_id, c.course_name, c.course_detail, c.price, c.course_image FROM Course c JOIN Course_Category cc ON c.course_id = cc.course_id WHERE cc.category_id = :categoryId AND c.course_id <> :courseId ORDER BY c.course_id DESC LIMIT 5", nativeQuery = true)
    List<Course> findRelatedCourses(@Param("categoryId") int categoryId, @Param("courseId") int courseId);
}
