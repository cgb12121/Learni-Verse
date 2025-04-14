package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.model.UserQuizz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizzRepository extends JpaRepository<UserQuizz,Integer> {
    List<UserQuizz> findByQuizz(Quizz quizz);

    Optional<UserQuizz> findTopByUser_UsernameAndQuizz_QuizzIdOrderByUserQuizzIdDesc(String username, int quizzId);

    Optional<UserQuizz> findByUser_UsernameAndQuizz_QuizzId(String username, int quizzId);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserQuizz u WHERE u.quizz.id = :quizId")
    void deleteByQuizzId(@Param("quizId") int quizId);
}
