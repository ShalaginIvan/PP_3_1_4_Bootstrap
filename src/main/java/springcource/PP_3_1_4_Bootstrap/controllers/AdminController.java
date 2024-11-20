package springcource.PP_3_1_4_Bootstrap.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springcource.PP_3_1_4_Bootstrap.model.User;
import springcource.PP_3_1_4_Bootstrap.services.RoleService;
import springcource.PP_3_1_4_Bootstrap.services.UserService;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    // метод добавления в модель общих данных
    private void addCommonModelAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = (User) authentication.getPrincipal();

        // Добавление текущего пользователя в модель
        // отображаем в шапке email и role
        model.addAttribute("loggedInUser", userAuth);

        // Преобразуем Set ролей в строку, разделенную запятой
        String rolesString = userAuth.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(", "));

        // Добавление текущего пользователя и его роли в модель
        model.addAttribute("loggedInUser", userAuth);
        model.addAttribute("loggedInRoles", rolesString);

        // добавляем список всех пользователей в модель
        model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", roleService.getAllRoles());
    }

    // Метод проверки выбранных ролей и введенных паролей
    private void checkRolesAndPasswords (User user, BindingResult result) {
        // Если нет ролей, добавляем ошибку
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            result.rejectValue("roles", "error.user", "Please select at least one role.");
        }

        // Если пароли не совпадают
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.user", "Password and Confirm_Password do not match");
        }
    }

    @GetMapping("/users")
    public String getUsersList(Model model) {
        addCommonModelAttributes(model);

        // добавляем пустого пользователя
        model.addAttribute("user", new User());

        return "/admin/usersList";
    }

    @PostMapping("/users")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

        addCommonModelAttributes(model);

        if (result.hasErrors() || (user.getRoles() == null || user.getRoles().isEmpty())
                || !user.getPassword().equals(user.getPasswordConfirm())) {

            // Если есть ошибка, добавляем ID пользователя для открытия модального окна
            model.addAttribute("errorUserNewId", 0);

            checkRolesAndPasswords (user, result);

            return "/admin/usersList";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохраняем пользователя с проверкой уникальности
        if (!userService.save(user)) {
            // Если есть ошибка, добавляем ID пользователя для открытия модального окна
            model.addAttribute("errorUserNewId", 0);

            result.rejectValue("email", "error.user", "User with that email already exists");
            return "/admin/usersList";
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/users/update")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

        addCommonModelAttributes(model);

        if (result.hasErrors() || (user.getRoles() == null || user.getRoles().isEmpty())
                || !user.getPassword().equals(user.getPasswordConfirm())) {

            // Если есть ошибка, добавляем ID пользователя для открытия модального окна
            model.addAttribute("errorUserId", user.getId());

            checkRolesAndPasswords (user, result);

            return "/admin/usersList";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/user/delete")
    public String delete(@RequestParam("id") Long id) {

        userService.delete(id);
        return "redirect:/admin/users";
    }

}
