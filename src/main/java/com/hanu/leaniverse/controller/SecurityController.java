package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.dto.UserDTO;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class SecurityController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String homePage(Model model) {
        return "homePage1";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            User user = (User) userService.loadUserByUsername(userDTO.getUsername());

            if (user == null) {
                model.addAttribute("error", "Wrong username or password!");
                return "login";
            }

            if (user.getRole().equals("TUTOR") || user.getRole().equals("ROLE_TUTOR")) {
                return "tutor/dashboard";
            }

            if (user.getRole().equals("ADMIN") || user.getRole().equals("ROLE_ADMIN")) {
                return "admin/users";
            }

            return "homePage1";

        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Wrong username or password!");
        }
        return "login";
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signup";
    }

    @PostMapping("/sign-up")
    public String signup(UserDTO userDTO, Model model) {
        User user = userService.signUpAccount(userDTO);
        if (user != null) {
            return "redirect:/login";
        }
        model.addAttribute("error", "Signup failed. Let's try again.");
        return "signup";
    }
}
