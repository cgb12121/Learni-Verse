package com.hanu.leaniverse.controller;


import com.hanu.leaniverse.model.Course;
import com.hanu.leaniverse.model.Enrollment;
import com.hanu.leaniverse.repository.CourseRepository;
import com.hanu.leaniverse.repository.EnrollmentRepository;
import com.hanu.leaniverse.service.UserService;
import com.hanu.leaniverse.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping
public class PaymentController {
        @Autowired
        private VNPayService vnPayService;
        @Autowired
        private CourseRepository courseRepository;
        @Autowired
        private UserService userService;
        @Autowired
private EnrollmentRepository enrollmentRepository;
        @PostMapping("/submit-order")
        public String submidOrder(@RequestParam("amount") int orderTotal,
                                  @RequestParam("orderInfo") String orderInfo,
                                  HttpServletRequest request
                                  ){
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
           return "redirect:" + vnpayUrl;
        }

        @GetMapping("/vnpay-payment-return")
        public String paymentCompleted(HttpServletRequest request){
            int paymentStatus =vnPayService.orderReturn(request);

            String orderInfo = request.getParameter("vnp_OrderInfo");
            String transactionId = request.getParameter("vnp_TransactionNo");
            String  totalPrice = request.getParameter("vnp_Amount");
            String bank = request.getParameter("vnp_BankCode");
            String cardType = request.getParameter("vnp_CardType");
            for(int i= 0; i<orderInfo.split(",").length;i++) {
                String id = orderInfo.split(",")[i].split(":")[0];
                Course course = courseRepository.findById(Integer.parseInt(id)).get();
                Enrollment enrollment = new Enrollment();
                enrollment.setBank(bank);
                enrollment.setPrice(Integer.parseInt(totalPrice)/100);
                enrollment.setCardType(cardType);
                enrollment.setCreateAt(new Date());
                enrollment.setTransactionNumber(transactionId);
                enrollment.setCourse(course);
                enrollment.setUser(userService.getCurrentUser(SecurityContextHolder.getContext().getAuthentication()));
                enrollment.setOrderInfo(orderInfo);
                enrollmentRepository.saveAndFlush(enrollment);
            }
            return paymentStatus == 1 ? "ordersuccess" : "orderfail";
        }
}

