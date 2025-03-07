package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Cart;
import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.repository.CartRepository;
import com.hanu.leaniverse.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserService userService;
    @Override
    public boolean addCartService(int courseId, Authentication authentication) {
        Course course = courseRepository.findById(courseId).get();
        User user = userService.getCurrentUser(authentication);
        if(cartRepository.findCartByUserAndCourse(user.getUserId(), courseId)==null){
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setCourse(course);
            cartRepository.saveAndFlush(cart);
            return true;

        }
        else {
            return false;
        }
    }
}
