package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Enrollment;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    public Map<LocalDate, List<Enrollment>> getEnrollmentsGroupedByDate(User user) {
        List<Enrollment> enrollments = enrollmentRepository.findByUser(user);

        return enrollments.stream()
                .collect(Collectors.groupingBy(enrollment ->
                        enrollment.getCreateAt().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                ));
    }

    public List<Enrollment> getAllEnrollmentsByUser(User user) {
        return enrollmentRepository.findByUser(user);
    }

    public boolean isEnrolled(int userId, int courseId) {
        return enrollmentRepository.isUserEnrolled(userId, courseId);
    }

    public void saveAndFlush(Enrollment enrollment) {
        enrollmentRepository.saveAndFlush(enrollment);
    }
}
