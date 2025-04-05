package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.model.UserSensitiveInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSensitiveInformationRepository extends JpaRepository<UserSensitiveInformation, Integer> {
    Optional<UserSensitiveInformation> findByUser(User user);

}
