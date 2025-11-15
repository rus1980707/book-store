package spring.bookstore.service;

import spring.bookstore.dto.UserLoginRequestDto;
import spring.bookstore.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto request);
}
