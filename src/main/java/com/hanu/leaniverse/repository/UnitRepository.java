package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
    List<Unit> findByCourse(Course course);
}
