package Laba3.controller;

import Laba3.entities.User;
import Laba3.entities.Worker;
import Laba3.securityconfig.CustomUserDetails;
import Laba3.service.EmailService;
import Laba3.service.ReportService;
import Laba3.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthController {
    private final UserService userService;
    private final ReportService reportService;
    private final EmailService emailService;

    public AuthController(UserService userService, ReportService reportService, EmailService emailService) {
        this.userService = userService;
        this.reportService = reportService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String showHome() {
        return "WJ24MHHstart";
    }

    @GetMapping("/login")
    public String loginWithMessage(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout,
                                   @RequestParam(value = "sessionExpired", required = false) String sessionExpired,
                                   Model model) {
        System.out.println("ShowLogin");
        return "auth/login";
    }

    @PostMapping("/login")
    public String login() {
        System.out.println("ShowLogin");
        return "auth/login";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllUsers(Model model) {
        System.out.println("getAllUsers");
        model.addAttribute("users", userService.getAllUsers());
        return "auth/users";  // відображає сторінку users.html
    }

    @GetMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable int id) {
        User user = userService.getUserById(id);
        userService.deleteUser(id);
        return "redirect:/users";  // Після видалення повертаємось до списку працівників
    }

    @GetMapping("/send-report")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String sendReportToEmail(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("userDetailsReportSend:" + userDetails);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null) {
            String email = ((CustomUserDetails) principal).getEmail();
            try {
                String filePath = reportService.generateReport();

                emailService.sendEmailWithAttachment(email, "Departments and workers Report", "Please find the attached report", filePath);
                return "redirect:/departments";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error_crud_message", "Failed to send report: " + e.getMessage());
                model.addAttribute("ret_page", "/departments"); // Сторінка, куди повернутись
                return "crud_errors.html";
            }

        }
        model.addAttribute("error_crud_message",  "No authenticated user found.");
        model.addAttribute("ret_page", "/departments"); // Сторінка, куди повернутись
        return "crud_errors.html";
    }
}
