package com.hanu.leaniverse.service.user;


import com.hanu.leaniverse.model.Enrollment;
import com.hanu.leaniverse.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EnrollmentService {

    List<Enrollment> getAllEnrollmentsByUser(User user);
    Map<LocalDate, List<Enrollment>> getEnrollmentsGroupedByDate(User user);
    boolean isEnrolled(int userId, int courseId);
    void saveAndFlush(Enrollment enrollment);
}
