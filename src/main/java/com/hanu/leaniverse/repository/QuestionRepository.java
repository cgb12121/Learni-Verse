package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM question WHERE quizz_id = ?1",
           nativeQuery = true)
  public List<Question> findQuestionsByQuizzId(int quizzId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Question q WHERE q.quizz.id = :quizId")
    void deleteByQuizzId(@Param("quizId") int quizId);
}
