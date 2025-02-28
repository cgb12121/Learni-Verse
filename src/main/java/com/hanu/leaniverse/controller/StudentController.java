package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.Choice;
import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.model.Quizz;
import com.hanu.leaniverse.repository.ChoiceRepository;
import com.hanu.leaniverse.repository.QuestionRepository;
import com.hanu.leaniverse.repository.QuizzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {
    @Autowired
    QuizzRepository quizzRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ChoiceRepository choiceRepository;
    @GetMapping("/quizz")
    public String showAllQuizzInAUnitPage(Model model,@RequestParam("unitId") String unitId){

        List<Quizz> quizzes = quizzRepository.findQuizzByUnitId(Integer.parseInt(unitId));
        if(quizzes != null) {
            model.addAttribute("quizzes", quizzes);
        }
        return "quizz-test";
    }
    @GetMapping("/question")
    public String showAllQuestionInAQuizz(Model model, @RequestParam("quizzId") String quizzId){
        List<Question> questions = questionRepository.findQuestionsByQuizzId(Integer.parseInt(quizzId));
        List<Choice> choices = new ArrayList<Choice>();
        for(int i = 0; i<questions.size();i++){
            choices.addAll(questions.get(i).getChoices());
        }
        model.addAttribute("questions",questions);
        model.addAttribute("choices",choices);

        return "question-test";
    }


}
