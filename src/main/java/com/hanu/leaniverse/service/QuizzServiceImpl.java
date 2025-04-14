package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.model.Unit;
import com.hanu.leaniverse.repository.QuestionRepository;
import com.hanu.leaniverse.repository.QuizzRepository;
import com.hanu.leaniverse.repository.UnitRepository;
import com.hanu.leaniverse.repository.UserQuizzRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuizzServiceImpl implements QuizzService {
    @Autowired
    QuizzRepository quizzRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserQuizzRepository userQuizzRepository;


    @Override
    public List<Quizz> findQuizzByUnitId(int unitId) {
        return quizzRepository.findQuizzByUnitId(unitId) ;
    }

    @Override
    public Quizz createQuiz(int unitId, String quizName) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        Quizz quizz = new Quizz();
        quizz.setUnit(unit);
        quizz.setQuizzName(quizName);
        quizz.setCreateAt(new Date());
        return quizzRepository.save(quizz);
    }

    @Override
    public Quizz updateQuiz(int quizId, String quizName) {
        Quizz quizz = quizzRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizz.setQuizzName(quizName);
        return quizzRepository.save(quizz);
    }

    @Override
    @Transactional
    public void deleteQuiz(int quizId) {
        Quizz quizz = quizzRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        userQuizzRepository.deleteByQuizzId(quizId);
        questionRepository.deleteByQuizzId(quizId);
        quizzRepository.delete(quizz);
    }

    public Quizz getQuizzById(int quizzId) {
        return quizzRepository.findById(quizzId).get();
    }

    public Optional<Quizz> getQuizzOptionalById(int quizzId) {
        return quizzRepository.findById(quizzId);
    }

    public Question addQuestionToQuiz(int quizzId, Question question) {
        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow(() -> new IllegalArgumentException("Invalid quizzId"));
        question.setQuizz(quizz);
        return questionRepository.save(question);
    }

    public void deleteQuestion(int questionId) {
       questionRepository.deleteById(questionId);
    }
}
