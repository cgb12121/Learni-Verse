package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Enrollment;
import com.hanu.leaniverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("SELECT DISTINCT e FROM Enrollment e WHERE e.user = :user")
    List<Enrollment> findByUser(@Param("user") User user);

}
