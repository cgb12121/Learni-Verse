package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
