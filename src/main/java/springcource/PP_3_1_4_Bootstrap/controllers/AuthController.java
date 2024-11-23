package springcource.PP_3_1_4_Bootstrap.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springcource.PP_3_1_4_Bootstrap.model.User;
import springcource.PP_3_1_4_Bootstrap.services.RoleService;
import springcource.PP_3_1_4_Bootstrap.services.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
   private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
            return "/auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@Valid @ModelAttribute("user") User user, BindingResult result) {

        if (result.hasErrors()) {
            return "/auth/registration";
        }

        // Проверяем Password and Confirm_Password
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.user", "Password and Confirm_Password do not match");
            return "/auth/registration";
        }

        // при регистрации даем роль -  ROLE_USER
        user.setRoles(Collections.singleton(roleService.get("ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!userService.save(user)){
            result.rejectValue("email", "error.user", "User with that email already exists");
            return "/auth/registration";
        }

        return "redirect:/auth/login";
    }
}
