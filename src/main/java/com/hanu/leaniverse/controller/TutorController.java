package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.service.UserService;
import com.hanu.leaniverse.service.tutor.TutorService;
import com.hanu.leaniverse.service.tutor.TutorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UserService userService;

    @Autowired
    public TutorController(TutorServiceImpl tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
//        User user = (User) authentication.getPrincipal();
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/login";
        }
        model.addAttribute("courses", tutorService.getCoursesForTutor(tutor));
        model.addAttribute("tutor", tutor);
        return "tutor/dashboard";
    }

    @GetMapping("/course/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "tutor/course_form";
    }

    @PostMapping("/course")
    public String createCourse(@ModelAttribute("course") Course course, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        tutorService.createCourse(course, tutor);
        return "redirect:/tutor/dashboard";
    }

    @GetMapping("/course/{courseId}/edit")
    public String showEditCourseForm(@PathVariable("courseId") int courseId, Model model, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Tutor tutor = tutorService.getTutorFromAuthentication(user);
        if (tutor == null) {
            return "redirect:/tutor/dashboard";
        }
        Course course = tutorService.getCourseById(courseId);
        if (course == null || tutorService.hasAccessToCourse(tutor, course)) {
            return "redirect:/tutor/dashboard";
        }
        model.addAttribute("course", course);
        model.addAttribute("units", tutorService.getUnitsForCourse(course));
        return "tutor/edit_course";
    }

    @PostMapping("/course/{courseId}")
    public String updateCourse(@PathVariable("courseId") int courseId, @ModelAttribute("course") Course updatedCourse, Authentication authentication) {
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

    @PostMapping("/course/{courseId}/delete")
    public String deleteCourse(@PathVariable("courseId") int courseId, Authentication authentication) {
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
    public String viewStudents(@PathVariable("courseId") int courseId, Model model, Authentication authentication) {
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

    @PostMapping("/course/{courseId}/addUnit")
    public String addUnit(@PathVariable("courseId") int courseId, @RequestParam("description") String description, Authentication authentication) {
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
    public String uploadVideo(@PathVariable("unitId") int unitId, @RequestParam("description") String description,
                              @RequestParam("file") MultipartFile file, Authentication authentication) {
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
    public String uploadQuiz(@PathVariable("unitId") int unitId, @RequestParam("quizzName") String quizzName, Authentication authentication) {
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
    public String viewQuizGrades(@PathVariable("quizzId") int quizzId, Model model, Authentication authentication) {
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
}