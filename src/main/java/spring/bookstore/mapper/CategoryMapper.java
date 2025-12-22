package spring.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import spring.bookstore.dto.CategoryDto;
import spring.bookstore.dto.CategoryRequestDto;
import spring.bookstore.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    void updateFromCategoryRequestDto(CategoryRequestDto categoryRequestDto, @MappingTarget
            Category category);
}
