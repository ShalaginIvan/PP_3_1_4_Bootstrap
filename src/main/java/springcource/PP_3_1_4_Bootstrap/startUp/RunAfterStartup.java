package springcource.PP_3_1_4_Bootstrap.startUp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import springcource.PP_3_1_4_Bootstrap.model.User;
import springcource.PP_3_1_4_Bootstrap.services.RoleService;
import springcource.PP_3_1_4_Bootstrap.services.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class RunAfterStartup {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {

        System.out.println("The application is running!");

        // если база пуста, то создаем для теста базу из 4 пользователей и таблицу ролей
        if (userService.getCountUsers() == 0) {
            // если база пуста, то создаем для теста базу из 4 пользователей и таблицу ролей

            // создаем роли
            roleService.create("ROLE_ADMIN");
            roleService.create("ROLE_USER");

            // admin/user
            User user1 = new User("Рустам", "Башаев", 20, "kata@mail.ru", "1234");
            user1.setRoles(new HashSet<>(Arrays.asList(roleService.get("ROLE_ADMIN"), roleService.get("ROLE_USER"))));

            // user
            User user2 = new User("Mike", "Tyson", 30, "mikeTyson@gmail.ru", "1234");
            user2.setRoles(Collections.singleton(roleService.get("ROLE_USER")));

            // admin/user
            User user3 = new User("Jon", "Smith", 40, "smith@list.ru", "1234");
            user3.setRoles(new HashSet<>(Arrays.asList(roleService.get("ROLE_ADMIN"), roleService.get("ROLE_USER"))));

            // admin
            User user4 = new User("Олег", "Иванов", 50, "oleg-ivanov@gmail.com", "1234");
            user4.setRoles(Collections.singleton(roleService.get("ROLE_ADMIN")));

            user1.setPassword(passwordEncoder.encode(user1.getPassword()));
            userService.save(user1);
            user2.setPassword(passwordEncoder.encode(user2.getPassword()));
            userService.save(user2);
            user3.setPassword(passwordEncoder.encode(user3.getPassword()));
            userService.save(user3);
            user4.setPassword(passwordEncoder.encode(user4.getPassword()));
            userService.save(user4);

            System.out.println("A new database has been created!");
        } else {
            System.out.println("We use the existing database!");
        }
    }
}
