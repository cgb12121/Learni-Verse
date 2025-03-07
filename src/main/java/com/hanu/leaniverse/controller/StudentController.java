package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import com.hanu.leaniverse.service.CourseService;
import com.hanu.leaniverse.service.ReviewService;
import com.hanu.leaniverse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class StudentController {
    @Autowired
    QuizzRepository quizzRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ChoiceRepository choiceRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;
    

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


    @GetMapping("/home-page")
    public String showHomePage(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Model model) {

        Map<String, Object> data = courseService.getHomePageData(title, categoryId);

        model.addAttribute("courses", data.get("courses"));
        model.addAttribute("categories", data.get("categories"));
        model.addAttribute("courseRatings", data.get("courseRatings"));
        return "homePage";
    }

    @GetMapping("/course-detail")
    public String showCourseDetail(@RequestParam("courseId") int courseId, Model model) {

        Map<String, Object> data = courseService.getCourseDetailData(courseId);

        model.addAttribute("course", data.get("course"));
        model.addAttribute("tutor", data.get("tutor"));
        model.addAttribute("reviews", data.get("reviews"));
        model.addAttribute("averageRating", data.get("averageRating"));
        model.addAttribute("relatedCourses", data.get("relatedCourses"));
        return "courseDetail";
    }

    @GetMapping("/write-review")
    public String showReviewForm(@RequestParam("courseId") Integer courseId,
                                 Model model,
                                 Authentication authentication) {
        if (userService.getCurrentUser(authentication) == null) {
            return "redirect:/login";
        }

        if (courseId == null) {
            return "redirect:/home-page";
        }

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setCourseId(courseId);
        model.addAttribute("reviewDTO", reviewDTO);
        model.addAttribute("courseId", courseId);
        return "writeReview";
    }

    @PostMapping("/submit-review")
    public String submitReview(@ModelAttribute("reviewDTO") ReviewDTO reviewDTO,
                               BindingResult result,
                               Authentication authentication) {
        if (result.hasErrors()) {
            return "writeReview";
        }

        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        reviewService.addReview(reviewDTO, currentUser);
        return "redirect:/course-detail?courseId=" + reviewDTO.getCourseId();
    }


}
