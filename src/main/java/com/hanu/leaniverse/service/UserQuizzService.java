package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.User_Quizz;
import org.springframework.security.core.Authentication;

public interface UserQuizzService {
    public User_Quizz setUserQuizz(int quizzId, double grade, Authentication authentication);
}
