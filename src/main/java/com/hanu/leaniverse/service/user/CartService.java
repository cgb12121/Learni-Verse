package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.model.Cart;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CartService {
    public boolean addCartService(int courseId, Authentication authentication);
    List<Cart> getAllCartByUserId(int userId);
    void deleteByCartId(int cartId);
}
