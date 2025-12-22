package spring.bookstore.service;

import spring.bookstore.dto.UserLoginRequestDto;
import spring.bookstore.dto.UserLoginResponseDto;
import spring.bookstore.model.User;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto request);

    User getCurrentUser();
}
