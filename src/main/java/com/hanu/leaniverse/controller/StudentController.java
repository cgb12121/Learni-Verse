package com.hanu.leaniverse.controller;


import com.hanu.leaniverse.dto.ReviewDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class StudentController {
    @Autowired
    ReviewService reviewService;
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

    @Autowired
    CourseService courseService;
    @GetMapping("/home-page")
    public String showHomePage(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Model model) {

        Map<String, Object> data = courseService.getHomePageData(title, categoryId);

        model.addAttribute("courses", data.get("courses"));
        model.addAttribute("categories", data.get("categories"));
        model.addAttribute("courseRatings", data.get("courseRatings"));
        return "homePage";
    }

    @GetMapping("/course-detail")
    public String showCourseDetail(@RequestParam("courseId") int courseId, Model model) {

        Map<String, Object> data = courseService.getCourseDetailData(courseId);
        model.addAttribute("reviewDTO", new ReviewDTO());
        model.addAttribute("course", data.get("course"));
        model.addAttribute("tutor", data.get("tutor"));
        model.addAttribute("reviews", data.get("reviews"));
        model.addAttribute("averageRating", data.get("averageRating"));
        model.addAttribute("relatedCourses", data.get("relatedCourses"));
        return "courseDetail";
    }

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

    @GetMapping("/show-cart")
    public String showCart(Model model){
        model.addAttribute("CartList",cartRepository.findAllCartByUser(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()));
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addCart(Model model, @RequestParam("courseId") int courseId, Authentication authentication ) throws Exception{
        if(cartService.addCartService(courseId, authentication)){
            return "redirect:/course-detail?courseId=" + courseId;
        }
        else {
            model.addAttribute("existedInCart", true);
            return "redirect:/course-detail?courseId=" + courseId;
        }
    }
    @PostMapping("/delete-cart-item")
    public String deleteCart(Model model, @RequestParam("cartId") int cartId){
        cartRepository.deleteById(cartId);
        return "redirect:/show-cart";
    }
    @GetMapping("/delete-cart-item")
    public String updateAfterDeleteCart(){
        return "redirect:/showCart";
    }
    @GetMapping("/show-wish-list")
    public String showWishList(Model model,Authentication authentication){
        model.addAttribute("WishList",wishListService.showWishList(authentication));
        return "wishList";
    }
    @PostMapping("/add-to-wish-list")
    public String addWishCourse(Model model,@RequestParam("courseId") int courseId,Authentication authentication) throws Exception{

        if(wishListService.addToWishList(courseId,authentication)){
            return "redirect:/course-detail?courseId=" + courseId;
        }
        else {
            return "redirect:/course-detail?courseId=" + courseId;
        }
    }
    @PostMapping("/delete-wish-list-item")
    public String deleteWishListItem(Model model, @RequestParam("WishListId") int wishListId){
        wishListService.deleteFromWishList(wishListId);
        return "redirect:/show-wish-list";
    }
    @GetMapping("/write-review")
    public String showReviewForm(@RequestParam("courseId") Integer courseId,
                                 Model model,
                                 Authentication authentication) {
        if (userService.getCurrentUser(authentication) == null) {
            return "redirect:/login";
        }

        if (courseId == null) {
            return "redirect:/home-page";
        }

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setCourseId(courseId);
        model.addAttribute("reviewDTO", reviewDTO);
        model.addAttribute("courseId", courseId);
        return "writeReview";
    }

    @PostMapping("/submit-review")
    public String submitReview(@ModelAttribute("reviewDTO") ReviewDTO reviewDTO,
                               BindingResult result,
                               Authentication authentication) {
        if (result.hasErrors()) {
            return "writeReview";
        }

        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        reviewService.addReview(reviewDTO, currentUser);
        return "redirect:/course-detail?courseId=" + reviewDTO.getCourseId();
    }
}
