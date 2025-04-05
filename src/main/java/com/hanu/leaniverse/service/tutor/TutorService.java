package com.hanu.leaniverse.service.tutor;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorService {

    @Value("${upload.dir}")
    private String uploadDir;

    private final TutorRepository tutorRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final TeachRepository teachRepository;
    private final UnitRepository unitRepository;
    private final VideoRepository videoRepository;
    private final QuizzRepository quizzRepository;
    private final UserQuizzRepository userQuizzRepository;

    public Tutor getTutorFromAuthentication(User user) {
        return tutorRepository.findByUser(user);
    }

    public List<Course> getCoursesForTutor(Tutor tutor) {
        List<Teach> teaches = teachRepository.findByTutor(tutor);
        return teaches.stream().map(Teach::getCourse).collect(Collectors.toList());
    }

    public void createCourse(Course course, Tutor tutor) {
        courseRepository.save(course);
        Teach teach = new Teach();
        teach.setTutor(tutor);
        teach.setCourse(course);
        teach.setTeachTime(new Date());
        teachRepository.save(teach);
    }

    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    public List<Unit> getUnitsForCourse(Course course) {
        return unitRepository.findByCourse(course);
    }

    public boolean hasAccessToCourse(Tutor tutor, Course course) {
        return teachRepository.findByTutorAndCourse(tutor, course) == null;
    }

    public void updateCourse(Course course, Course updatedCourse) {
        course.setCourseName(updatedCourse.getCourseName());
        course.setCourseDetail(updatedCourse.getCourseDetail());
        course.setPrice(updatedCourse.getPrice());
        courseRepository.save(course);
    }

    public void deleteCourseAssociation(Tutor tutor, Course course) {
        Teach teach = teachRepository.findByTutorAndCourse(tutor, course);
        if (teach != null) {
            teachRepository.delete(teach);
        }
    }

    public List<User> getStudentsForCourse(Course course) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
        return enrollments.stream().map(Enrollment::getUser).collect(Collectors.toList());
    }

    public Unit getUnitById(int unitId) {
        return unitRepository.findById(unitId).orElse(null);
    }

    public void addUnit(Course course, String description) {
        Unit unit = new Unit();
        unit.setDescription(description);
        unit.setCourse(course);
        unitRepository.save(unit);
    }

    public void uploadVideo(Unit unit, String description, MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.isEmpty()) {
            throw new Exception("No file provided");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Video video = new Video();
        video.setDescription(description);
        video.setFilePath(filePath.toString());
        video.setUnit(unit);
        videoRepository.save(video);
    }

    public void uploadQuiz(Unit unit, String quizzName) {
        Quizz quizz = new Quizz();
        quizz.setQuizzName(quizzName);
        quizz.setUnit(unit);
        quizzRepository.save(quizz);
    }

    public Quizz getQuizzById(int quizzId) {
        return quizzRepository.findById(quizzId).orElse(null);
    }

    public List<UserQuizz> getQuizGrades(Quizz quizz) {
        return userQuizzRepository.findByQuizz(quizz);
    }
}