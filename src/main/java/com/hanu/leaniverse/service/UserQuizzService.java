package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.UserQuizz;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserQuizzService {
    public UserQuizz setUserQuizz(int quizzId, double grade, Authentication authentication);

    Optional<UserQuizz> getUserQuizzByUsernameAndQuizzId(String username, int quizzId);
}
