package springcource.PP_3_1_4_Bootstrap.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springcource.PP_3_1_4_Bootstrap.model.Role;
import springcource.PP_3_1_4_Bootstrap.repositories.RoleRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleAdmin() {
        // Проверяем, существует ли роль ROLE_ADMIN в базе данных
        Optional<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdmin.isEmpty()) {
            // Если роли нет, создаем её
            roleAdmin = Optional.of(new Role(1L, "ROLE_ADMIN"));
            roleRepository.save(roleAdmin.get());
        }
        return roleAdmin.get();
    }

    public Role getRoleUser() {
        // Проверяем, существует ли роль ROLE_USER в базе данных
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser.isEmpty()) {
            // Если роли нет, создаем её
            roleUser = Optional.of(new Role(2L, "ROLE_USER"));
            roleRepository.save(roleUser.get());
        }
        return roleUser.get();
    }

    public Set<Role> getAllRoles() {
        return Set.of(getRoleAdmin(), getRoleUser());
    }
}
