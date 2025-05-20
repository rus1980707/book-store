package spring.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.bookstore.dto.UserRegistrationRequestDto;
import spring.bookstore.dto.UserResponseDto;
import spring.bookstore.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegistrationRequestDto request);

    UserResponseDto toDto(User user);
}
