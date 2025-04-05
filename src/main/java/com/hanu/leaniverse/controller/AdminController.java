package com.hanu.leaniverse.controller;

import com.hanu.leaniverse.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{userId}/role")
    public String updateUserRole(@PathVariable("userId") int userId, @RequestParam("role") String role) {
        adminService.updateUserRole(userId, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") int userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", adminService.getAllCourses());
        return "admin/courses";
    }

    @PostMapping("/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable("courseId") int courseId) {
        adminService.deleteCourse(courseId);
        return "redirect:/admin/courses";
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", adminService.getAllCategories());
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam("name") String name, @RequestParam("description") String description) {
        adminService.createCategory(name, description);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") int categoryId) {
        adminService.deleteCategory(categoryId);
        return "redirect:/admin/categories";
    }

    @GetMapping("/reports")
    public String listReports(Model model) {
        model.addAttribute("reports", adminService.getAllReports());
        return "admin/reports";
    }

    @PostMapping("/reports/{reportId}/resolve")
    public String resolveReport(@PathVariable("reportId") int reportId) {
        adminService.resolveReport(reportId);
        return "redirect:/admin/reports";
    }
}