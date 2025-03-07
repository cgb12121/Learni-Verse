package com.hanu.leaniverse.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface CartService {
    public boolean addCartService(int courseId, Authentication authentication);
}
