package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.dto.QuestionDTO;

import java.util.List;

public interface GradingService {
    double Grading(List<QuestionDTO> questions_choices);
}
