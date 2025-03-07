package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.repository.QuizzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizzServiceImpl implements QuizzService {
    @Autowired
    QuizzRepository quizzRepository;
    @Override
    public List<Quizz> findQuizzByUnitId(int unitId) {
        return quizzRepository.findQuizzByUnitId(unitId) ;
    }
}
