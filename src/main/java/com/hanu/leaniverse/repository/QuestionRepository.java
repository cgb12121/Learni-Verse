package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM question WHERE quizz_id = ?1",
           nativeQuery = true)
  public List<Question> findQuestionsByQuizzId(int quizzId);
}
