package spring.bookstore.service;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.bookstore.dto.UserRegistrationRequestDto;
import spring.bookstore.dto.UserResponseDto;
import spring.bookstore.exception.RegistrationException;
import spring.bookstore.mapper.UserMapper;
import spring.bookstore.model.Role;
import spring.bookstore.model.User;
import spring.bookstore.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User already exist with particular email: "
                    + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = new Role();
        userRole.setName(Role.RoleName.ROLE_USER);
        user.getRoles().add(userRole);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        return userMapper.toDto(userRepository.save(user));
    }
}
