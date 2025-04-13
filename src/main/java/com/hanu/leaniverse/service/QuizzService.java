package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.model.Quizz;

import java.util.List;
import java.util.Optional;

public interface QuizzService {
    public List<Quizz> findQuizzByUnitId(int unitId);

    Quizz createQuiz(int unitId, String quizName);

    Quizz updateQuiz(int quizId, String quizName);

    void deleteQuiz(int quizId);
    Quizz getQuizzById(int quizzId);
    Question addQuestionToQuiz(int quizzId, Question question);
    void deleteQuestion(int questionId);
    Optional<Quizz> getQuizzOptionalById(int quizzId);
}
