package spring.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Isbn is required")
    private String isbn;

    @NotNull(message = "price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @Size(max = 200, message = "Cover image URL must be at most 200 characters")
    private String coverImage;
}
