package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.CategoryRepository;
import com.hanu.leaniverse.repository.CourseRepository;
import com.hanu.leaniverse.repository.UnitRepository;
import com.hanu.leaniverse.service.QuizzService;
import com.hanu.leaniverse.service.UserService;
import com.hanu.leaniverse.service.tutor.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private QuizzService quizzService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/login";
        }
        model.addAttribute("courses", tutorService.getCoursesForTutor(tutor));
        model.addAttribute("tutor", tutor);
        model.addAttribute("newCourse", new Course());
        model.addAttribute("categories", categoryRepository.findAll());
        return "tutor/dashboard";
    }

    @GetMapping("/course/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "tutor/course_form";
    }

    @PostMapping("/course")
    public String createCourse(@ModelAttribute("course") Course course,
                               @RequestParam("categoryIds") List<Integer> categoryIds,
                               Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);

        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }

        tutorService.createCourse(course, tutor, categoryIds);
        return "redirect:/tutor/dashboard";
    }


    @PostMapping("/course/edit")
    public String updateCourse(@RequestParam("courseId") int courseId,
                               @ModelAttribute("course") Course updatedCourse,
                               Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course == null || tutorService.hasAccessToCourse(tutor, course)) {
            return "redirect:/tutor/dashboard";
        }
        tutorService.updateCourse(course, updatedCourse);
        return "redirect:/tutor/dashboard";
    }
    @GetMapping("/course")
    public String showCourse(@RequestParam("courseId") int courseId, Model model) {
        Course course = courseRepository.findById(courseId).get();
        model.addAttribute("course", course);
        return "tutor/courseDetail";
    }

    @PostMapping("/course/delete")
    public String deleteCourse(@RequestParam("courseId") int courseId, Authentication authentication) {

        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
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
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course == null || tutorService.hasAccessToCourse(tutor, course)) {
            return "redirect:/tutor/dashboard";
        }
        model.addAttribute("students", tutorService.getStudentsForCourse(course));
        model.addAttribute("course", course);
        return "tutor/student_list";
    }

    @GetMapping("/course/unit")
    public String showUnit(@RequestParam("unitId") int unitId, Model model, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/tutor/dashboard";
        }
        Unit unit = unitRepository.findById(unitId).get();
        model.addAttribute("unit", unit);
        return "tutor/unit-detail";
    }

    @PostMapping("/course/{courseId}/addUnit")
    public String addUnit(@PathVariable("courseId") int courseId,
                          @RequestParam("description") String description,
                          Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course == null || tutorService.hasAccessToCourse(tutor, course)) {
            return "redirect:/tutor/dashboard";
        }
        tutorService.addUnit(course, description);
        return "redirect:/tutor/course/" + courseId + "/edit";
    }

    @PostMapping("/unit/{unitId}/uploadVideo")
    public String uploadVideo(@PathVariable("unitId") int unitId,
                              @RequestParam("description") String description,
                              @RequestParam("file") MultipartFile file,
                              Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Unit unit = tutorService.getUnitById(unitId);
        if (unit == null || tutorService.hasAccessToCourse(tutor, unit.getCourse())) {
            return "redirect:/tutor/dashboard";
        }
        try {
            tutorService.uploadVideo(unit, description, file);
            return "redirect:/tutor/course/" + unit.getCourse().getCourseId() + "/edit";
        } catch (Exception e) {
            return "redirect:/tutor/course/" + unit.getCourse().getCourseId() + "/edit?error=" + (e.getMessage().equals("No file provided") ? "noFile" : "uploadFailed");
        }
    }

    @PostMapping("/unit/{unitId}/uploadQuiz")
    public String uploadQuiz(@PathVariable("unitId") int unitId,
                             @RequestParam("quizzName") String quizzName,
                             Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Unit unit = tutorService.getUnitById(unitId);
        if (unit == null || tutorService.hasAccessToCourse(tutor, unit.getCourse())) {
            return "redirect:/tutor/dashboard";
        }
        tutorService.uploadQuiz(unit, quizzName);
        return "redirect:/tutor/course/" + unit.getCourse().getCourseId() + "/edit";
    }

    @GetMapping("/quizz/{quizzId}/grades")
    public String viewQuizGrades(@PathVariable("quizzId") int quizzId,
                                 Model model,
                                 Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Quizz quizz = tutorService.getQuizzById(quizzId);
        if (quizz == null || tutorService.hasAccessToCourse(tutor, quizz.getUnit().getCourse())) {
            return "redirect:/tutor/dashboard";
        }
        model.addAttribute("userQuizzes", tutorService.getQuizGrades(quizz));
        model.addAttribute("quizz", quizz);
        return "tutor/quiz_grades";
    }

//    @GetMapping("/course/{courseId}/unit/{unitId}/quiz/new")
//    public String showCreateQuizForm(@PathVariable("courseId") int courseId,
//                                     @PathVariable("unitId") int unitId,
//                                     Model model,
//                                     Authentication authentication) {
//        User user = userService.getCurrentUser(authentication);
//        if (user == null) {
//            return "redirect:/tutor/dashboard";
//        }
//
//        model.addAttribute("quiz", new Quizz());
//        model.addAttribute("unitId", unitId);
//        model.addAttribute("courseId", courseId);
//        return "tutor/quiz_form";
//    }

    @PostMapping("/course/{courseId}/unit/{unitId}/quiz")
    public String createQuiz(@PathVariable("courseId") int courseId,
                             @PathVariable("unitId") int unitId,
                             @ModelAttribute("quiz") Quizz quiz,
                             Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/tutor/dashboard";
        }

        quizzService.createQuiz(unitId, quiz.getQuizzName());
        return "redirect:/tutor/quiz_form";
    }

    @GetMapping("/quiz/{quizId}/edit")
    public String showEditQuizForm(@PathVariable("quizId") int quizId,
                                   Model model,
                                   Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/tutor/dashboard";
        }

        Quizz quiz = quizzService.findQuizzByUnitId(
                        quizzService.findQuizzByUnitId(quizId).get(0).getUnit().getUnitId()
                ).stream().filter(q -> q.getQuizzId() == quizId).findFirst()
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        model.addAttribute("quiz", quiz);
        model.addAttribute("courseId", quiz.getUnit().getCourse().getCourseId());
        return "tutor/quiz_edit_form";
    }

    @PostMapping("/quiz/{quizId}")
    public String updateQuiz(@PathVariable("quizId") int quizId,
                             @ModelAttribute("quiz") Quizz updatedQuiz,
                             Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/tutor/dashboard";
        }

        Quizz quiz = quizzService.updateQuiz(quizId, updatedQuiz.getQuizzName());
        return "redirect:/tutor/quiz_edit_form";
    }

    @PostMapping("/quiz/{quizId}/delete")
    public String deleteQuiz(@PathVariable("quizId") int quizId,
                             Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
//
//        Quizz quiz = quizzService.findQuizzByUnitId(
//                        quizzService.findQuizzByUnitId(quizId).get(0).getUnit().getUnitId()
//                ).stream().filter(q -> q.getQuizzId() == quizId).findFirst()
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        int courseId = quiz.getUnit().getCourse().getCourseId();
        quizzService.deleteQuiz(quizId);
        return "redirect:/tutor/dashboard";
    }
}