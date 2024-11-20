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

    private void initDataBase() {
        if (userService.getCountUsers() == 0) {
            // если база пуста, то создаем для теста базу из 4 пользователей и таблицу ролей
            // админ
            User user1 = new User("Рустам", "Башаев", 30, "kata@mail.ru", "1234");
            user1.setRoles(new HashSet<>(Arrays.asList(roleService.getRoleAdmin(), roleService.getRoleUser())));

            // user
            User user2 = new User("Mike", "Tyson", 40, "mikeTyson@gmail.ru", "1234");
            user2.setRoles(Collections.singleton(roleService.getRoleUser()));

            // админ
            User user3 = new User("Jon", "Smith", 50, "smith@list.ru", "1234");
            user3.setRoles(new HashSet<>(Arrays.asList(roleService.getRoleAdmin(), roleService.getRoleUser())));

            // user
            User user4 = new User("Олег", "Иванов", 60, "oleg@admin.com", "1234");
            user4.setRoles(Collections.singleton(roleService.getRoleAdmin()));

            user1.setPassword(passwordEncoder.encode(user1.getPassword()));
            userService.save(user1);
            user2.setPassword(passwordEncoder.encode(user2.getPassword()));
            userService.save(user2);
            user3.setPassword(passwordEncoder.encode(user3.getPassword()));
            userService.save(user3);
            user4.setPassword(passwordEncoder.encode(user4.getPassword()));
            userService.save(user4);
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        // если база пуста, то создаем для теста базу из 4 пользователей и таблицу ролей
        initDataBase();
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
        user.setRoles(Collections.singleton(roleService.getRoleUser()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!userService.save(user)){
            result.rejectValue("email", "error.user", "User with that email already exists");
            return "/auth/registration";
        }

        return "redirect:/auth/login";
    }
}
