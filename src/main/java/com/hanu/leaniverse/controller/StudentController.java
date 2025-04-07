package com.hanu.leaniverse.controller;


import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.dto.QuestionDTO;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import com.hanu.leaniverse.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentService enrollmentService;
    @Autowired
    UserSensitiveInformationRepository userSensitiveInformationRepository;

    @GetMapping("/shopping-history")
    public String showHistoryPage(Model model, Authentication authentication){
        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }
        Map<LocalDate, List<Enrollment>> enrollmentsByDate = enrollmentService.getEnrollmentsGroupedByDate(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("enrollmentsByDate", enrollmentsByDate);
        return "shoppingHistory";
    }
    @GetMapping("/home-page")
    public String showHomePage(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Model model) {

        Map<String, Object> data = courseService.getHomePageData(title, categoryId);

        model.addAttribute("courses", data.get("courses"));
        model.addAttribute("categories", data.get("categories"));
        model.addAttribute("courseRatings", data.get("courseRatings"));
        return "homePage1";
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
    public String showCart(Model model, Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("CartList",cartRepository.findAllCartByUser(userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()));
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addCart(Model model, @RequestParam("courseId") int courseId, Authentication authentication ) throws Exception{
        if(cartService.addCartService(courseId, authentication)){
            String message = "Course successfully added to cart!";
            boolean success = true;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        }
        else {
            String message = "Course is already in the cart!";
            boolean success = false;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        }
    }
    @PostMapping("/delete-cart-item")
    public String deleteCart(Model model, @RequestParam("cartId") int cartId){
        cartRepository.deleteById(cartId);
        String message = "Course was deleted successfully!";
        boolean success = true;
        return "redirect:/show-cart?message=" + message + "&success=" +success;
    }
    @GetMapping("/delete-cart-item")
    public String updateAfterDeleteCart(Model model){
        String message = "Course was deleted successfully!";
        boolean success = true;
        return "redirect:/showCart?message=" +message + "&success="+success;
    }
    @GetMapping("/show-wish-list")
    public String showWishList(Model model,Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("WishList",wishListService.showWishList(authentication));
        return "wishList";
    }
    @PostMapping("/add-to-wish-list")
    public String addWishCourse(Model model,@RequestParam("courseId") int courseId,Authentication authentication) throws Exception{

        if(wishListService.addToWishList(courseId,authentication)){
            String message = "Course successfully added to wish list!";
            boolean success = true;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        }
        else {
            String message = "Course is already in the wish list!";
            boolean success = false;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        }
    }
    @PostMapping("/delete-wish-list-item")
    public String deleteWishListItem(Model model, @RequestParam("WishListId") int wishListId){
        wishListService.deleteFromWishList(wishListId);
        return "redirect:/show-wish-list";
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
    @GetMapping("/profile")
    public String getUserProfile(Model model, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        UserSensitiveInformation userInfo = user.getUserSensitiveInformation();
        if (userInfo == null) {
            userInfo = new UserSensitiveInformation();
            userInfo.setUser(user);
        }

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        return "profile";
    }

    @PostMapping("/edit-profile")
    public String updateUserProfile(@ModelAttribute("userInfo") UserSensitiveInformation userInfo,
                                    @RequestParam("fullName") String fullName,
                                    Authentication authentication) {
        userService.updateUserProfile(userInfo, fullName, authentication);
        return "redirect:/profile";
    }
}
