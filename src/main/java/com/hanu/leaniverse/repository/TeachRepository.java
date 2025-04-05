package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.Teach;
import com.hanu.leaniverse.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachRepository extends JpaRepository<Teach, Integer> {
    Teach findByTutorAndCourse(Tutor tutor, Course course);
    List<Teach> findByTutor(Tutor tutor);
}
