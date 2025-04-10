package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.repository.QuestionRepository;
import com.hanu.leaniverse.repository.QuizzRepository;
import com.hanu.leaniverse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("")
public class QuestionController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuizzRepository quizzRepository;

    @Autowired
    UserService userService;

    @GetMapping("/tutor/course/unit/quizz/{quizzId}")
    public String showAllQuestionEditPage(@PathVariable("quizzId") int quizzId, Model model, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        Quizz quizz = quizzRepository.findById(quizzId).get();
        Question question = new Question();
        model.addAttribute("quizz",quizz);
        model.addAttribute("questionForm",question);
        return "/tutor/quizz";
    }

//    @PostMapping("/addQuestion")
//    public String addQuestion(@RequestBody Question question){
//        questionRepository.save(question);
//        return "redirect:/showQuestionManagement";
//    }

    @PostMapping("/tutor/course/unit/quizz/{quizzId}/add-question")
    public String addQuestion(@ModelAttribute Question question, @PathVariable("quizzId") int quizzId, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow(() -> new IllegalArgumentException("Invalid quizzId"));
        question.setQuizz(quizz);
        questionRepository.save(question);
        return "redirect:/tutor/course/unit/quizz/" + quizzId;
    }

    @PostMapping("/tutor/course/unit/quizz/{quizzId}/delete-question/{questionId}")
    public String deleteQuestion(@PathVariable int questionId, @PathVariable int quizzId, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        questionRepository.deleteById(questionId);
        return "redirect:/tutor/course/unit/quizz/" + quizzId;
    }
}
