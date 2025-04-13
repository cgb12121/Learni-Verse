package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CartService {
    public boolean addCartService(int courseId, Authentication authentication);
    List<Cart> getAllCartByUserId(int userId);
    void deleteByCartId(int cartId);
}
