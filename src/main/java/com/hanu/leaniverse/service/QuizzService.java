package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Quizz;

import java.util.List;

public interface QuizzService {
    public List<Quizz> findQuizzByUnitId(int unitId);
}
