package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestionByQuizId(int quizzId);
}
