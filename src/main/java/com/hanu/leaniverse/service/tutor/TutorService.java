package com.hanu.leaniverse.service.tutor;

import com.hanu.leaniverse.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TutorService {
    Tutor getTutorFromAuthentication(User user);
    List<Course> getCoursesForTutor(Tutor tutor);
    void createCourse(Course course, Tutor tutor);
    Course getCourseById(int courseId);
    List<Unit> getUnitsForCourse(Course course);
    boolean hasAccessToCourse(Tutor tutor, Course course);
    void updateCourse(Course course, Course updatedCourse);
    void deleteCourseAssociation(Tutor tutor, Course course);
    List<User> getStudentsForCourse(Course course);
    Unit getUnitById(int unitId);
    void addUnit(Course course, String description);
    void uploadVideo(Unit unit, String description, MultipartFile file) throws Exception;
    void uploadQuiz(Unit unit, String quizzName);
    Quizz getQuizzById(int quizzId);
    List<UserQuizz> getQuizGrades(Quizz quizz);
}
