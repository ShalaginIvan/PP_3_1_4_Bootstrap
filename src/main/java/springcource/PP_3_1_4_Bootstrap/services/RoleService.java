package springcource.PP_3_1_4_Bootstrap.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springcource.PP_3_1_4_Bootstrap.model.Role;
import springcource.PP_3_1_4_Bootstrap.repositories.RoleRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Set<Role> getAll() {
        return new HashSet<>(roleRepository.findAll());
    }

    public Role get(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Роль " + roleName + " не найдена!"));
    }
    public boolean create(String roleName) {
        Optional<Role> roleFromBD = roleRepository.findByName(roleName);
        if (!roleFromBD.isEmpty()) {
            return false;
        }
        roleRepository.save(new Role(roleName));
        return true;
    }
}
