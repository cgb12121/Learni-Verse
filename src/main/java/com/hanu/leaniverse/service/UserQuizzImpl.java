package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.UserQuizz;
import com.hanu.leaniverse.repository.QuizzRepository;
import com.hanu.leaniverse.repository.UserQuizzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserQuizzImpl implements UserQuizzService {
    @Autowired
QuizzRepository quizzRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserQuizzRepository userQuizzRepository;
    @Override
    public UserQuizz setUserQuizz(int quizzId, double grade, Authentication authentication) {
        UserQuizz user_quizz = new UserQuizz();
        user_quizz.setQuizz(quizzRepository.findById(quizzId).get());
        user_quizz.setGrade(grade);
        user_quizz.setUser(userService.getCurrentUser(authentication));
        userQuizzRepository.saveAndFlush(user_quizz);
        return null;
    }
}
