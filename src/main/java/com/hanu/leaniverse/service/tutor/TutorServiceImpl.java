package com.hanu.leaniverse.service.tutor;

import com.hanu.leaniverse.dto.CourseDTO;
import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private TeachRepository teachRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private UserQuizzRepository userQuizzRepository;
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${upload.dir.courses}")
    private String courseUploadDir;

    public Tutor getTutorFromAuthentication(User user) {
        return tutorRepository.findByUser(user);
    }

    public List<Course> getCoursesForTutor(Tutor tutor) {
        List<Teach> teaches = teachRepository.findByTutor(tutor);
        return teaches.stream().map(Teach::getCourse).collect(Collectors.toList());
    }

    public void createCourse(Course course, Tutor tutor, List<Integer> categoryIds) {
        courseRepository.save(course);

        List<CourseCategory> courseCategoryList = new ArrayList<>();
        for (int categoryId : categoryIds) {
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setCourse(course);
            courseCategory.setCategory(categoryRepository.findById(categoryId).orElse(null));
            courseCategoryList.add(courseCategory);
        }

        courseCategoryRepository.saveAll(courseCategoryList);

        Teach teach = new Teach();
        teach.setTutor(tutor);
        teach.setCourse(course);
        teach.setTeachTime(new Date());
        teachRepository.save(teach);

        System.out.println("Course and categories saved successfully!");
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

        List<CourseCategory> courseCategories = courseCategoryRepository.findByCourse(course);
        courseCategoryRepository.deleteAll(courseCategories); // Delete categories linked to this course

        Teach teach = teachRepository.findByTutorAndCourse(tutor, course);
        if (teach != null) {
            teachRepository.delete(teach);
        }
        courseRepository.delete(course);

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
        quizz.setCreateAt(new Date());
        quizzRepository.save(quizz);
    }
    public void deleteUnit(int unitId){
        unitRepository.deleteById(unitId);
    }
    public Quizz getQuizzById(int quizzId) {
        return quizzRepository.findById(quizzId).orElse(null);
    }

    public List<UserQuizz> getQuizGrades(Quizz quizz) {
        return userQuizzRepository.findByQuizz(quizz);
    }

    @Override
    public String saveCourseImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + imageFile.getOriginalFilename();

        Path uploadPath = Paths.get(courseUploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, Paths.get(courseUploadDir + storageFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        return storageFileName;
    }

    private void deleteCourseImage(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        Path filePath = Paths.get(courseUploadDir + fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    @Override
    public void createCourseWithImage(Course course, Tutor tutor, List<Integer> categoryIds, MultipartFile courseImage) throws IOException {
        if (courseImage != null && !courseImage.isEmpty()) {
            String fileName = saveCourseImage(courseImage);
            course.setCourseImage(fileName);
        }

        createCourse(course, tutor, categoryIds);
    }

    @Override
    public void updateCourseWithImage(Course existingCourse, Course updatedCourse, MultipartFile courseImage) throws IOException {
        if (courseImage != null && !courseImage.isEmpty()) {
            if (existingCourse.getCourseImage() != null) {
                deleteCourseImage(existingCourse.getCourseImage());
            }

            String fileName = saveCourseImage(courseImage);
            updatedCourse.setCourseImage(fileName);
        } else {
            updatedCourse.setCourseImage(existingCourse.getCourseImage());
        }


        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setCourseDetail(updatedCourse.getCourseDetail());
        existingCourse.setPrice(updatedCourse.getPrice());

        courseRepository.save(existingCourse);
    }

    @Override
    public void createCourseFromDTO(CourseDTO courseDTO, Tutor tutor, List<Integer> categoryIds) throws IOException {
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setCourseDetail(courseDTO.getCourseDetail());
        course.setPrice(courseDTO.getPrice());

        MultipartFile courseImageFile = courseDTO.getCourseImage();
        createCourseWithImage(course, tutor, categoryIds, courseImageFile);
    }

    @Override
    public void updateCourseFromDTO(int courseId, CourseDTO courseDTO, Tutor tutor) throws IOException, IllegalAccessException {
        Course existingCourse = getCourseById(courseId);
        if (existingCourse == null || hasAccessToCourse(tutor, existingCourse)) {
            throw new IllegalAccessException("Tutor does not have access to this course or course does not exist");
        }

        Course updatedCourse = new Course();
        updatedCourse.setCourseName(courseDTO.getCourseName());
        updatedCourse.setCourseDetail(courseDTO.getCourseDetail());
        updatedCourse.setPrice(courseDTO.getPrice());

        MultipartFile courseImage = courseDTO.getCourseImage();
        updateCourseWithImage(existingCourse, updatedCourse, courseImage);
    }
}