package spring.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import spring.bookstore.validation.FieldMatch;

@Data
@FieldMatch(message = "Passwords must match", fields = {})
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 30)
    private String password;

    @NotBlank
    @Length(min = 8, max = 30)
    private String repeatPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String shippingAddress;
}
