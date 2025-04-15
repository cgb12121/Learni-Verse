package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.dto.CourseDTO;
import com.hanu.leaniverse.model.*;


import com.hanu.leaniverse.service.tutor.TutorService;
import com.hanu.leaniverse.service.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private QuizzService quizzService;

    @Autowired
    private VideoService videoService;


    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        model.addAttribute("courses", tutorService.getCoursesForTutor(tutor));
        model.addAttribute("tutor", tutor);
        model.addAttribute("courseDTO", new CourseDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "tutor/dashboard";
    }

//    @GetMapping("/course/new")
//    public String showCreateCourseForm(Model model) {
//        model.addAttribute("course", new Course());
//        model.addAttribute("categories", categoryRepository.findAll());
//        return "tutor/course_form";
//    }

    @PostMapping("/course")
    public String createCourse(@ModelAttribute("courseDTO") CourseDTO courseDTO,
                               @RequestParam("categoryIds") List<Integer> categoryIds,
                               Authentication authentication,
                               Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }

        try {
            tutorService.createCourseFromDTO(courseDTO, tutor, categoryIds);
        } catch (IOException e) {
            model.addAttribute("error", "Error when saving: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("courseDTO", courseDTO);
            return "redirect:/tutor/dashboard";
        }

        return "redirect:/tutor/dashboard";
    }

    @PostMapping("/course/edit")
    public String updateCourse(@RequestParam("courseId") int courseId,
                               @ModelAttribute("courseDTO") CourseDTO courseDTO,
                               Authentication authentication,
                               Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }

        try {
            tutorService.updateCourseFromDTO(courseId, courseDTO, tutor);
        } catch (IOException e) {
            model.addAttribute("error", "Error uploading image: " + e.getMessage());
            model.addAttribute("course", tutorService.getCourseById(courseId));
            model.addAttribute("courseDTO", courseDTO);
            return "tutor/dashboard";
        } catch (IllegalAccessException e) {
            return "redirect:/tutor/dashboard";
        }

        return "redirect:/tutor/dashboard";
    }


    @PostMapping("/course/delete")
    public String deleteCourse(@RequestParam("courseId") int courseId, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course != null) {
            tutorService.deleteCourseAssociation(tutor, course);
        }
        return "redirect:/tutor/dashboard";
    }

    @GetMapping("/course/{courseId}/students")
    public String viewStudents(@PathVariable("courseId") int courseId,
                               Model model,
                               Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course == null || tutorService.hasAccessToCourse(tutor, course)) {
            return "redirect:/tutor/dashboard";
        }
        model.addAttribute("students", tutorService.getStudentsForCourse(course));
        model.addAttribute("course", course);
        return "tutor/student_list";
    }

    @GetMapping("/course/{courseId}/detail")
    public String showCourseDetail(@PathVariable("courseId") int courseId, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Course course = courseService.getCourseById(courseId);
        model.addAttribute("course", course);
        return "tutor/course_detail";
    }

    @GetMapping("/course/unit/{unitId}")
    public String showUnit(@PathVariable("unitId") int unitId, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Unit unit = unitService.getUnitById(unitId);
        model.addAttribute("unit", unit);
        return "tutor/unit-detail";
    }

    @PostMapping("/course/{courseId}/add-unit")
    public String addUnit(@PathVariable("courseId") int courseId,
                          @RequestParam("description") String description,
                          Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course == null || tutorService.hasAccessToCourse(tutor, course)) {
            return "redirect:/tutor/dashboard";
        }
        tutorService.addUnit(course, description);
        return "redirect:/tutor/course/" + courseId + "/detail";
    }
    @PostMapping("/course/{courseId}/detail/delete-unit")
    public String deleteUnit(@RequestParam("unitId") int unitId,
                          @PathVariable("courseId") int courseId,
                          Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        tutorService.deleteUnit(unitId);
        return "redirect:/tutor/course/" + courseId + "/detail";
    }
    @PostMapping("/unit/{unitId}/upload-video")
    public String uploadVideo(@PathVariable int unitId,
                              @RequestParam String filePath,
                              @RequestParam String description,
                              Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Video video = new Video();
        video.setDescription(description);
        video.setCreateAt(new Date());
        Unit unit = unitService.getUnitById(unitId);
        video.setUnit(unit);

        if (filePath.contains("youtube.com/watch?v=")) {
            filePath = filePath.split("v=")[1].split("&")[0];
        } else if (filePath.contains("youtu.be/")) {
            filePath = filePath.split("youtu.be/")[1].split("\\?")[0];
        }
        video.setFilePath(filePath);

        videoService.addNewVideo(video);

        return "redirect:/tutor/course/unit/" + unitId;
    }

    @PostMapping("/unit/{unitId}/delete-video/{videoId}")
    public String deleteVideo(@PathVariable("unitId") int unitId,
                              @PathVariable("videoId") int videoId,
                              Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        videoService.deleteVideoById(videoId);
        return "redirect:/tutor/course/unit/" + unitId;
    }

    @PostMapping("/unit/{unitId}/upload-quiz")
    public String uploadQuiz(@PathVariable("unitId") int unitId,
                             @RequestParam("quizzName") String quizzName,
                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Unit unit = tutorService.getUnitById(unitId);
        if (unit == null || tutorService.hasAccessToCourse(tutor, unit.getCourse())) {
            return "redirect:/tutor/dashboard";
        }
        tutorService.uploadQuiz(unit, quizzName);
        return "redirect:/tutor/course/unit/" + unitId;
    }

    @GetMapping("/quizz/{quizzId}/grades")
    public String viewQuizGrades(@PathVariable("quizzId") int quizzId,
                                 Model model,
                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Quizz quizz = tutorService.getQuizzById(quizzId);
        if (quizz == null || tutorService.hasAccessToCourse(tutor, quizz.getUnit().getCourse())) {
            return "redirect:/tutor/dashboard";
        }
        model.addAttribute("userQuizzes", tutorService.getQuizGrades(quizz));
        model.addAttribute("quizz", quizz);
        return "tutor/quiz_grades";
    }

    @PostMapping("/course/{courseId}/unit/{unitId}/quiz")
    public String createQuiz(@PathVariable("courseId") int courseId,
                             @PathVariable("unitId") int unitId,
                             @ModelAttribute("quiz") Quizz quiz,
                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }

        quizzService.createQuiz(unitId, quiz.getQuizzName());
        return "redirect:/tutor/quiz_form";
    }

    @PostMapping("/quiz/{quizId}")
    public String updateQuiz(@PathVariable("quizId") int quizId,
                             @ModelAttribute("quiz") Quizz updatedQuiz,
                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }

        Quizz quiz = quizzService.updateQuiz(quizId, updatedQuiz.getQuizzName());
        return "redirect:/tutor/quiz_edit_form";
    }

    @PostMapping("/course/unit/{unitId}/quiz/{quizId}/delete")
    public String deleteQuiz(@PathVariable("quizId") int quizId,
                             @PathVariable("unitId") int unitId,
                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        quizzService.deleteQuiz(quizId);
        return "redirect:/tutor/course/unit/" + unitId;
    }

    @GetMapping("/course/unit/quizz/{quizzId}")
    public String showAllQuestionEditPage(@PathVariable("quizzId") int quizzId, Model model, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Quizz quizz = quizzService.getQuizzById(quizzId);
        Question question = new Question();
        model.addAttribute("quizz",quizz);
        model.addAttribute("questionForm", question);
        return "/tutor/quizz";
    }
    @PostMapping("/course/unit/quizz/{quizzId}/add-question")
    public String addQuestion(@ModelAttribute Question question, @PathVariable("quizzId") int quizzId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        Question q = quizzService.addQuestionToQuiz(quizzId, question);
        return "redirect:/tutor/course/unit/quizz/" + quizzId;
    }

    @PostMapping("/course/unit/quizz/{quizzId}/delete-question/{questionId}")
    public String deleteQuestion(@PathVariable int questionId, @PathVariable int quizzId, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/";
        }
        quizzService.deleteQuiz(questionId);
        return "redirect:/tutor/course/unit/quizz/" + quizzId;
    }
}