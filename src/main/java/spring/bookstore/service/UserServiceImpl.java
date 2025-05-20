package spring.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.bookstore.dto.UserRegistrationRequestDto;
import spring.bookstore.dto.UserResponseDto;
import spring.bookstore.exception.RegistrationException;
import spring.bookstore.mapper.UserMapper;
import spring.bookstore.model.User;
import spring.bookstore.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User already exist with particular email");
        }
        User user = userMapper.toEntity(request);
        return userMapper.toDto(userRepository.save(user));
    }
}
