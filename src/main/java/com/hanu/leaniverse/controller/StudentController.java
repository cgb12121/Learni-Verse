package com.hanu.leaniverse.controller;


import com.hanu.leaniverse.dto.ReviewDTO;
import com.hanu.leaniverse.dto.QuestionDTO;

import com.hanu.leaniverse.model.*;
import com.hanu.leaniverse.repository.*;
import com.hanu.leaniverse.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;


@Controller
public class StudentController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private GradingService gradingService;
    @Autowired
    private QuizzService quizzService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CartService cartService;
    @Autowired
    private WishListService wishListService;
    @Autowired
    private UserQuizzService userQuizzService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/my-courses")
    public String showCourses(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Enrollment> enrollments = enrollmentService.getAllEnrollmentsByUser(currentUser);

        List<Course> courses = enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());

        model.addAttribute("courses", courses);

        return "my_courses";
    }

    @GetMapping("/course-detail")
    public String showCourseDetail(@RequestParam("courseId") int courseId, Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return "redirect:/home-page";
        }

        boolean isEnrolled = enrollmentService.isEnrolled(currentUser.getUserId(), courseId);
        if (!isEnrolled) {
            Map<String, Object> data = courseService.getCourseDetailData(courseId);
            model.addAttribute("reviewDTO", new ReviewDTO());
            model.addAttribute("course", data.get("course"));
            model.addAttribute("tutor", data.get("tutor"));
            model.addAttribute("reviews", data.get("reviews"));
            model.addAttribute("averageRating", data.get("averageRating"));
            model.addAttribute("relatedCourses", data.get("relatedCourses"));
            return "courseDetail";
        }

        model.addAttribute("course", course);
        model.addAttribute("units", course.getUnits());

        return "course_overview";
    }

    @GetMapping("/course-overview")
    public String showCourseOverview(@RequestParam("courseId") int courseId, Model model) {
        Map<String, Object> data = courseService.getCourseDetailData(courseId);
        model.addAttribute("course", data.get("course"));
        model.addAttribute("tutor", data.get("tutor"));
        model.addAttribute("reviews", data.get("reviews"));
        model.addAttribute("averageRating", data.get("averageRating"));
        model.addAttribute("relatedCourses", data.get("relatedCourses"));

        return "course_overview";
    }

    @GetMapping("/learning/start")
    public String startCourse(@RequestParam("courseId") int courseId, Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }
        Course course = courseService.getCourseById(courseId);
        String videoLink = "";

        if (course != null && course.getUnits() != null && !course.getUnits().isEmpty()) {

            Unit firstUnit = course.getUnits().get(0);

            if (firstUnit.getVideo() != null && !firstUnit.getVideo().isEmpty()) {
                Video firstVideo = firstUnit.getVideo().get(0);
                videoLink = firstVideo.getFilePath();
            }

            List<Quizz> allQuizzes = new ArrayList<>();
            for (Unit unit : course.getUnits()) {
                List<Quizz> quizzes = quizzService.findQuizzByUnitId(unit.getUnitId());
                allQuizzes.addAll(quizzes);
            }

            Map<Integer, Double> userGrades = new HashMap<>();


            for (Quizz quiz : allQuizzes) {

                Optional<UserQuizz> optionalUserQuizz = userQuizzService.getUserQuizzByUsernameAndQuizzId(currentUser.getUsername(), quiz.getQuizzId());


                if (optionalUserQuizz.isPresent()) {
                    UserQuizz userQuizz = optionalUserQuizz.get();

                    userGrades.put(quiz.getQuizzId(), userQuizz.getGrade());

                }
            }


            model.addAttribute("quizzes", allQuizzes);
            model.addAttribute("userGrades", userGrades);
        }
        model.addAttribute("course", course);
        model.addAttribute("currentVideoId", videoLink);

        return "video_page";
    }

    @GetMapping("/shopping-history")
    public String showHistoryPage(Model model, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }
        Map<LocalDate, List<Enrollment>> enrollmentsByDate = enrollmentService.getEnrollmentsGroupedByDate(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("enrollmentsByDate", enrollmentsByDate);
        return "shoppingHistory";
    }

    @GetMapping("")
    public String showHomePage(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Model model) {

        Map<String, Object> data = courseService.getHomePageData(title, categoryId);

        model.addAttribute("courses", data.get("courses"));
        model.addAttribute("categories", data.get("categories"));
        model.addAttribute("courseRatings", data.get("courseRatings"));
        return "homePage1";
    }

    //    @GetMapping("/quizz")
//    public String showAllQuizzInAUnitPage(Model model,@RequestParam("unitId") int unitId){
//
//        List<Quizz> quizzes = quizzService.findQuizzByUnitId(unitId);
//        if(quizzes != null) {
//            model.addAttribute("quizzes", quizzes);
//        }
//
//        return "quizz-test";
//    }
    @GetMapping("/question")
    public String showAllQuestionInAQuizz(Model model, @RequestParam("quizzId") int quizzId, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        List<Question> questions = questionService.getAllQuestionByQuizId(quizzId);
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        model.addAttribute("questions", questions);
        model.addAttribute("quizzId", quizzId);
        return "do_quizz_test";
    }

    @GetMapping("/show-cart")
    public String showCart(Model model, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("CartList", cartService.getAllCartByUserId(user.getUserId()));
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addCart(Model model, @RequestParam("courseId") int courseId, Authentication authentication) throws Exception {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        if (cartService.addCartService(courseId, authentication)) {
            String message = "Course successfully added to cart!";
            boolean success = true;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        } else {
            String message = "Course is already in the cart!";
            boolean success = false;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        }
    }

    @PostMapping("/delete-cart-item")
    public String deleteCart(Model model, @RequestParam("cartId") int cartId, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        cartService.deleteByCartId(cartId);
        String message = "Course was deleted successfully!";
        boolean success = true;
        return "redirect:/show-cart?message=" + message + "&success=" + success;
    }

    //    @GetMapping("/delete-cart-item")
//    public String updateAfterDeleteCart(Model model, Authentication authentication){
//        User user = userService.getCurrentUser(authentication);
//        if (user == null) {
//            return "redirect:/login";
//        }
//        String message = "Course was deleted successfully!";
//        boolean success = true;
//        return "redirect:/show-cart?message=" +message + "&success="+success;
//    }
    @GetMapping("/show-wish-list")
    public String showWishList(Model model, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("WishList", wishListService.showWishList(authentication));
        return "wishList";
    }

    @PostMapping("/add-to-wish-list")
    public String addWishCourse(Model model, @RequestParam("courseId") int courseId, Authentication authentication) throws Exception {

        if (wishListService.addToWishList(courseId, authentication)) {
            String message = "Course successfully added to wish list!";
            boolean success = true;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        } else {
            String message = "Course is already in the wish list!";
            boolean success = false;
            return "redirect:/course-detail?courseId=" + courseId + "&message=" + message + "&success=" + success;
        }
    }

    @PostMapping("/delete-wish-list-item")
    public String deleteWishListItem(Model model, @RequestParam("WishListId") int wishListId) {
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

    @GetMapping("/unit-test/{quizzId}")
    public String showAllQuestionInAQuizz(@PathVariable("quizzId") int quizzId, Model model) {
        List<Question> questions = questionService.getAllQuestionByQuizId(quizzId);
        model.addAttribute("questions", questions);
        model.addAttribute("quizzId", quizzId);

        // Lấy quiz title từ DB
        Optional<Quizz> quizOptional = quizzService.getQuizzOptionalById(quizzId);
        if (quizOptional.isPresent()) {
            String quizTitle = quizOptional.get().getQuizzName();
            model.addAttribute("quizTitle", quizTitle);
        } else {
            model.addAttribute("quizTitle", "Quiz"); // fallback nếu không có
        }

        return "do_quizz_test";
    }

    @PostMapping("/grade")
    public ResponseEntity<List<Map<String, Object>>> gradeTheQuizz(@RequestBody List<QuestionDTO> submittedAnswers, @RequestParam int quizzId, Authentication authentication) {
        List<Question> originalQuestions = questionService.getAllQuestionByQuizId(quizzId);

        List<Map<String, Object>> resultList = new ArrayList<>();
        int correctCount = 0;

        for (QuestionDTO userAnswer : submittedAnswers) {
            Question q = originalQuestions.stream()
                    .filter(question -> question.getQuestionId() == userAnswer.getQuestionId())
                    .findFirst()
                    .orElse(null);

            if (q == null) continue;

            boolean isCorrect =
                    userAnswer.isChoice1() == q.getIsChoice1() &&
                            userAnswer.isChoice2() == q.getIsChoice2() &&
                            userAnswer.isChoice3() == q.getIsChoice3() &&
                            userAnswer.isChoice4() == q.getIsChoice4() &&
                            userAnswer.isChoice5() == q.getIsChoice5();

            if (isCorrect) correctCount++;

            Map<String, Object> qResult = new HashMap<>();
            qResult.put("questionId", q.getQuestionId());
            qResult.put("userAnswer", userAnswer);
            qResult.put("correctAnswer", Map.of(
                    "choice1", q.getIsChoice1(),
                    "choice2", q.getIsChoice2(),
                    "choice3", q.getIsChoice3(),
                    "choice4", q.getIsChoice4(),
                    "choice5", q.getIsChoice5()
            ));
            resultList.add(qResult);
        }

        double grade = (double) correctCount / originalQuestions.size() * 100;
        userQuizzService.setUserQuizz(quizzId, grade, authentication);

        return ResponseEntity.ok(resultList);
    }
}
