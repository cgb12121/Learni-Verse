package com.hanu.leaniverse.service.admin;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ReportRepository reportRepository;

    public AdminService() {
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserRole(int userId, String role) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
        }
    }

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        categoryRepository.save(category);
    }

    public void deleteCategory(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public void resolveReport(int reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report != null) {
            report.setResolved(true);
            reportRepository.save(report);
        }
    }
}