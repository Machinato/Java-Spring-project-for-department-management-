package Laba3.controller;

import Laba3.entities.User;
import Laba3.service.RoleService;
import Laba3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final RoleService roleService;
    private UserService userService;
    @Autowired
    ApplicationContext context;

    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        System.out.println("showRegistrationForm");
        model.addAttribute("user", new User());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid User regUser,
            BindingResult result,
            @ModelAttribute("confirmPassword") String confirmPassword,
            Model model) {

        System.out.println("");
        // Перевірка на помилки валідації
        if (result.hasErrors()) {
            return "auth/registration";
        }

        // Перевірка збігу паролів
        if (!regUser.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", true);
            return "auth/registration";
        }

        // Перевірка унікальності користувача
        if (userService.findUserByName(regUser.getUsername()) != null) {
            result.rejectValue("username", "error.user", "Ім'я користувача вже існує");
            return "auth/registration";
        }

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        regUser.setRole(roleService.getRoleById(2));
        regUser.setPassword(passwordEncoder.encode(regUser.getPassword()));
        // Збереження користувача
        userService.saveUser(regUser);
        return "redirect:/login";
    }
}
