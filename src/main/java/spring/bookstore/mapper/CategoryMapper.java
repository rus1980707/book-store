package spring.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.bookstore.dto.CategoryDto;
import spring.bookstore.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
