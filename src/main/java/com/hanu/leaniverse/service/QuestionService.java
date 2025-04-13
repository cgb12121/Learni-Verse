package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestionByQuizId(int quizzId);
}
