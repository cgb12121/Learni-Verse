package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.dto.QuestionDTO;
import com.hanu.leaniverse.model.Question;
import com.hanu.leaniverse.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GradingServiceImpl implements GradingService {
    @Autowired
    QuestionRepository questionRepository;
     public double Grading(List<QuestionDTO> questions_choices){
        double gradeEach =10/ questions_choices.size();
        System.out.println("size"+questions_choices.size());
        int wrong =0;
        for(int i = 0; i<questions_choices.size();i++) {
            QuestionDTO question_choice = questions_choices.get(i);
            Question question = questionRepository.findById(question_choice.getQuestionId()).get();
            System.out.println(questions_choices.get(i).isChoice2());
            if (questions_choices.get(i).isChoice1() && !question.getIsChoice1()) {
                wrong++;
                continue;
            }
            if (questions_choices.get(i).isChoice2() && !question.getIsChoice2()) {
                wrong++;
                continue;
            }
            if (questions_choices.get(i).isChoice3() && !question.getIsChoice3()) {
                wrong++;
                continue;
            }
            if (questions_choices.get(i).isChoice4() && !question.getIsChoice4()) {
                wrong++;
                continue;
            }
            if (questions_choices.get(i).isChoice5() && !question.getIsChoice5()) {
                wrong++;
                continue;
            }
            if (!questions_choices.get(i).isChoice1() && !questions_choices.get(i).isChoice2()
                    && !questions_choices.get(i).isChoice3() && !questions_choices.get(i).isChoice4()
                    && !questions_choices.get(i).isChoice5()) {
                wrong++;
                continue;
            }


        }
        double grade = 10 - wrong*gradeEach;
        return grade;
    }
}
