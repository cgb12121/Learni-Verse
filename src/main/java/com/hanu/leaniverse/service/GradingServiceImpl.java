package com.hanu.leaniverse.service;

import com.hanu.leaniverse.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GradingServiceImpl implements GradingService {
    @Autowired
    ChoiceRepository choiceRepository;
     public double Grading(List<String> questions_choices){
        double gradeEach =10/ questions_choices.size();
        System.out.println("size"+questions_choices.size());
        int wrong =0;
        for(int i = 0; i<questions_choices.size();i++){
            String[] a =questions_choices.get(i).split(",");
            System.out.println(a[0]);
            for(int j = 0; j<a.length;j++){

                if(choiceRepository.findById(Integer.parseInt(a[j])).get().isTrue()==0){
                    wrong = wrong+1;
                    System.out.println(wrong);
                    break;
                }
            }
        }
        double grade = 10 - wrong*gradeEach;
        return grade;
    }
}
