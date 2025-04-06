package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.repository.QuestionRepository;
import com.hanu.leaniverse.repository.QuizzRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/show-question-management")
    public String showAllQuestionEditPage(@RequestParam int quizzId, Model model){
        List<Question> list = questionRepository.findQuestionsByQuizzId(quizzId);
        Question question = new Question();
        model.addAttribute("questions",list);
        model.addAttribute("questionForm",question);
        return "questionManagePage";
    }
//    @PostMapping("/addQuestion")
//    public String addQuestion(@RequestBody Question question){
//        questionRepository.save(question);
//        return "redirect:/showQuestionManagement";
//    }
@PostMapping("/add-question")
public String addQuestion(@ModelAttribute Question question, @RequestParam int quizzId) {
    Quizz quizz = quizzRepository.findById(quizzId).orElseThrow(() -> new IllegalArgumentException("Invalid quizzId"));
    question.setQuizz(quizz);
    questionRepository.save(question);
    return "redirect:/showQuestionManagement?quizzId=" + quizzId;
}

    @PostMapping("/delete-question")
    public String deleteQuestion(@RequestParam int questionId, @RequestParam int quizzId){
        questionRepository.deleteById(questionId);
        return "redirect:/showQuestionManagement?quizzId=" + quizzId;
    }
}
