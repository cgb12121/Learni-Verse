package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query(value = "SELECT * FROM cart WHERE user_id =?1 ",
    nativeQuery = true)
    public List<Cart> findAllCartByUser(int userId);

    @Query(value = "SELECT * FROM cart c WHERE c.user_id = ?1 AND c.course_id =?2",
    nativeQuery = true)
    public Cart findCartByUserAndCourse(int userId, int courseId);

}
