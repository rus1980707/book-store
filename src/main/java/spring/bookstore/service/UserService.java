package spring.bookstore.service;

import spring.bookstore.dto.UserRegistrationRequestDto;
import spring.bookstore.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
