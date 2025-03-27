package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.dto.QuestionDTO;
import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import com.hanu.leaniverse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
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
    QuizzService quizzService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CartService cartService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    WishListService wishListService;
    @Autowired
    UserQuizzService userQuizzService;
    @Autowired
    UserService userService;
    @GetMapping("/quizz")
    public String showAllQuizzInAUnitPage(Model model,@RequestParam("unitId") int unitId){

        List<Quizz> quizzes = quizzService.findQuizzByUnitId(unitId);
        if(quizzes != null) {
            model.addAttribute("quizzes", quizzes);
        }

        return "quizz-test";
    }
    @GetMapping("/question")
    public String showAllQuestionInAQuizz(Model model, @RequestParam("quizzId") int quizzId){
        List<Question> questions = questionRepository.findQuestionsByQuizzId(quizzId);
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        model.addAttribute("questions",questions);
        model.addAttribute("quizzId",quizzId);
        return "do_quizz_test";
    }
    @PostMapping("/grade")
    public String gradeTheQuizz(Model model, @RequestBody List<QuestionDTO> questionDTOS, @RequestParam int quizzId, Authentication authentication){
        double grade = gradingService.Grading(questionDTOS);
        userQuizzService.setUserQuizz(quizzId,grade,authentication);
        model.addAttribute("grade",grade);
        return "hello" ;
    }

    @GetMapping("/showCart")
    public String showCart(Model model){
        model.addAttribute("CartList",cartRepository.findAllCartByUser(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()));
        return "sucess";
    }

    @PostMapping("/addToCart")
    public String addCart(Model model, @RequestParam("courseId") int courseId, Authentication authentication ) throws Exception{
        if(cartService.addCartService(courseId, authentication)){
            return "success";
        }
        else throw new Exception("can not add cart");
    }
    @PostMapping("/deleteCartItem")
    public String deleteCart(Model model, @RequestParam("cartId") int cartId){
        cartRepository.deleteById(cartId);
        return "success";
    }
    @GetMapping("/showWishList")
    public String showWishList(Model model,Authentication authentication){
        model.addAttribute("WishList",wishListService.showWishList(authentication));
        return null;
    }
    @PostMapping("/addToWishList")
    public String addWishCourse(Model model,@RequestParam("courseId") int courseId,Authentication authentication) throws Exception{

        if(wishListService.addToWishList(courseId,authentication)){
            return "successful";
        }
        else {
            throw new Exception("already exist in WishList");
        }
    }
    @PostMapping("/deleteWishListItem")
    public String deleteWishListItem(Model model, @RequestParam("WishListId") int wishListId){
        wishListService.deleteFromWishList(wishListId);
        return null;
    }


}
