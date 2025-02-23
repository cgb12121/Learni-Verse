package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Tutor;
import com.hanu.leaniverse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Integer> {
    boolean existsByUser(User user);
}