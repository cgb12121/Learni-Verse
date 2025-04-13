package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestionByQuizId(int quizzId) {
        return questionRepository.findQuestionsByQuizzId(quizzId);
    }
}
