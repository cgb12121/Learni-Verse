package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.model.Unit;
import com.hanu.leaniverse.repository.QuizzRepository;
import com.hanu.leaniverse.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QuizzServiceImpl implements QuizzService {
    @Autowired
    QuizzRepository quizzRepository;

    @Autowired
    private UnitRepository unitRepository;


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
    public void deleteQuiz(int quizId) {
        Quizz quizz = quizzRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizzRepository.delete(quizz);
    }
}
