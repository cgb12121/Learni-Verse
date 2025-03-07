package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.WishList;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface WishListService {
    public boolean addToWishList(int courseId, Authentication authentication);
    public boolean deleteFromWishList(int courseId);
    public List<WishList> showWishList(Authentication authentication);
}
