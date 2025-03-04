package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import com.hanu.leaniverse.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
@Controller
public class StudentController {
    @Autowired
    GradingService gradingService;
    @Autowired
    QuizzRepository quizzRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ChoiceRepository choiceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserQuizzRepository userQuizzRepository;
    @GetMapping("/quizz")
    public String showAllQuizzInAUnitPage(Model model,@RequestParam("unitId") int unitId){

        List<Quizz> quizzes = quizzRepository.findQuizzByUnitId(unitId);
        if(quizzes != null) {
            model.addAttribute("quizzes", quizzes);
        }
        return "quizz-test";
    }
    @GetMapping("/question")
    public String showAllQuestionInAQuizz(Model model, @RequestParam("quizzId") int quizzId){
        List<Question> questions = questionRepository.findQuestionsByQuizzId(quizzId);
        model.addAttribute("questions",questions);
        return "question-test";
    }
    @PostMapping("/grade")
    public String gradeTheQuizz(Model model, @RequestBody List<String> gradeList, @RequestParam int quizzId){
        double grade = gradingService.Grading(gradeList);
        User_Quizz user_quizz = new User_Quizz();
        user_quizz.setQuizz(quizzRepository.findById(quizzId).get());
        user_quizz.setGrade(grade);
        user_quizz.setUser(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        userQuizzRepository.saveAndFlush(user_quizz);
        model.addAttribute("grade",grade);
        return "hello" ;
    }

    @GetMapping("/showCart")
    public String showCart(Model model){
        model.addAttribute("CartList",cartRepository.findAllCartByUser(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()));
        return "sucess";
    }

    @PostMapping("/addToCart")
    public String addCart(Model model, @RequestParam("courseId") int courseId ) throws Exception{
        Course course = courseRepository.findById(courseId).get();
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(cartRepository.findCartByUserAndCourse(user.getUserId(), courseId)==null){
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setCourse(course);
            cartRepository.saveAndFlush(cart);
            return "successful";
        }
        else {
            throw new Exception("already exist in cart");
        }
    }
    @PostMapping("/deleteCartItem")
    public String deleteCart(Model model, @RequestParam("cartId") int cartId){
        cartRepository.deleteById(cartId);
        return "success";
    }
    @GetMapping("/showWishList")
    public String showWishList(Model model){
        model.addAttribute("WishList",wishListRepository.findAllWishListByUserId(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()));
        return null;
    }
    @PostMapping("/addToWishList")
    public String addWishCourse(Model model,@RequestParam("courseId") int courseId) throws Exception{
        Course course = courseRepository.findById(courseId).get();
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(wishListRepository.findWishListByUserIdAndCourse(user.getUserId(), courseId)==null){
            WishList wishList = new WishList();
            wishList.setUser(user);
            wishList.setCourse(course);
            wishListRepository.saveAndFlush(wishList);
            return "successful";
        }
        else {
            throw new Exception("already exist in cart");
        }
    }
    @PostMapping("/deleteWishListItem")
    public String deleteWishListItem(Model model, @RequestParam("WishListId") int wishListId){
        wishListRepository.deleteById(wishListId);
        return null;
    }


}
