package com.hanu.leaniverse.service;


import com.hanu.leaniverse.model.Enrollment;
import com.hanu.leaniverse.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    Map<LocalDate, List<Enrollment>> getEnrollmentsGroupedByDate(User user);
}
