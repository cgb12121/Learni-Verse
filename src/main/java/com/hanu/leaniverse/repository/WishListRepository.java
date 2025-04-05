package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer> {
    @Query(value = "SELECT * FROM wish_list where user_id =?1",
    nativeQuery = true)
    public List<WishList> findAllWishListByUserId(int id);
    @Query(value = "SELECT * FROM wish_list wl WHERE wl.user_id = ?1 AND wl.course_id =?2",
    nativeQuery = true)
    public WishList findWishListByUserIdAndCourse(int courseId, int userId);
}
