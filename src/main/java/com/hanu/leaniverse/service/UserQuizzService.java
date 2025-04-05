package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.UserQuizz;
import org.springframework.security.core.Authentication;

public interface UserQuizzService {
    public UserQuizz setUserQuizz(int quizzId, double grade, Authentication authentication);
}
