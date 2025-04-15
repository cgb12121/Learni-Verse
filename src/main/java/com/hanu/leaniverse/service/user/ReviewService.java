package com.hanu.leaniverse.service.user;


import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.model.Review;
import com.hanu.leaniverse.model.User;

public interface ReviewService {
    Review addReview(ReviewDTO reviewDTO, User user);



}
