package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.model.WishList;
import com.hanu.leaniverse.repository.CourseRepository;
import com.hanu.leaniverse.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class WishListImpl implements WishListService{
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserService userService;
    @Autowired
    WishListRepository wishListRepository;
    @Override
    public boolean addToWishList(int courseId, Authentication authentication) {
        Course course = courseRepository.findById(courseId).get();
        User user = userService.getCurrentUser(authentication);
        if(wishListRepository.findWishListByUserIdAndCourse(user.getUserId(), courseId)==null){
            WishList wishList = new WishList();
            wishList.setUser(user);
            wishList.setCourse(course);
            wishList.setCreateAt(new Date());
            wishListRepository.saveAndFlush(wishList);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean deleteFromWishList(int wishListId) {
        try {
            wishListRepository.deleteById(wishListId);
            return true;
        }
        catch (Exception exception){
            throw new RuntimeException("wish list delete error");
        }

    }

    @Override
    public List<WishList> showWishList(Authentication authentication) {
        return wishListRepository.findAllWishListByUserId(userService.getCurrentUser(authentication).getUserId());
    }
}
