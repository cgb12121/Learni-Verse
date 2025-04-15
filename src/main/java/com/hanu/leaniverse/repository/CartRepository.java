package com.hanu.leaniverse.repository;

import com.hanu.leaniverse.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query(value = "SELECT c.card_id, c.course_id, c.user_id\n" +
            "FROM cart c\n" +
            "WHERE c.user_id = 7  -- Replace 7 with the desired user_id\n" +
            "AND NOT EXISTS (\n" +
            "    SELECT 1 \n" +
            "    FROM enrollment e \n" +
            "    WHERE e.course = c.course_id \n" +
            "    AND e.user_id = c.user_id\n" +
            "); ",
    nativeQuery = true)
    public List<Cart> findAllCartByUser(int userId);

    @Query(value = "SELECT * FROM cart c WHERE c.user_id = ?1 AND c.course_id =?2",
    nativeQuery = true)
    public Cart findCartByUserAndCourse(int userId, int courseId);



}
