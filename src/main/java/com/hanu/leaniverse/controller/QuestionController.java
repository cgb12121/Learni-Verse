package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.repository.QuestionRepository;
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
    @GetMapping("/showQuestionManagement")
    public String showAllQuestionEditPage(@RequestParam int quizzId, Model model){
        List<Question> list = questionRepository.findQuestionsByQuizzId(quizzId);
        model.addAttribute("questions",list);
        return "QuestionManagePage";
    }
    @GetMapping("/showAddQuestionForm")
    public String showAddQuestionForm(Model model){
        Question question = new Question();
        model.addAttribute("question",question);
        return "AddQuestionPage";
    }
    @PostMapping("/addQuestion")
    public String addQuestion(@RequestBody Question question){
        questionRepository.save(question);
        return "redirect:/showQuestionManagement";
    }
    @PostMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam int questionId){
        questionRepository.deleteById(questionId);
        return "redirect:/showQuestionManagement";
    }
}
