package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.model.UserQuizz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizzRepository extends JpaRepository<UserQuizz,Integer> {
    List<UserQuizz> findByQuizz(Quizz quizz);
}
