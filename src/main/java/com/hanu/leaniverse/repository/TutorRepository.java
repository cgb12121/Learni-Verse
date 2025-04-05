package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Tutor;
import com.hanu.leaniverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Integer> {
    boolean existsByUser(User user);
    Tutor findByUser(User user);
}