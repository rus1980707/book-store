package spring.bookstore;

import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import spring.bookstore.model.Role;
import spring.bookstore.model.User;
import spring.bookstore.repository.RoleRepository;
import spring.bookstore.repository.UserRepository;
import spring.bookstore.security.JwtUtil;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = DockerComposeMysqlInitializer.class)
@Transactional
public abstract class AbstractIntegrationTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    protected HttpHeaders authHeaders(Role.RoleName roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer" + jwtUtil.generateToken(
                saveUser(roleName).getEmail()));
        return headers;
    }

    private User saveUser(Role.RoleName roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });
        User user = new User();
        user.setEmail(UUID.randomUUID() + "@test.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }
}
