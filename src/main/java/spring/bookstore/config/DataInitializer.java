package spring.bookstore.config;

import jakarta.annotation.PostConstruct;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.bookstore.model.Role;
import spring.bookstore.model.User;
import spring.bookstore.repository.RoleRepository;
import spring.bookstore.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Role adminRole = roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.RoleName.ROLE_ADMIN);
                    return roleRepository.save(role);
                });

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.RoleName.ROLE_USER);
                    return roleRepository.save(role);
                });

        if (userRepository.findByEmail("admin@bookstore.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@bookstore.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }
    }
}
