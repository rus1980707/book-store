package spring.bookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.bookstore.dto.CategoryDto;
import spring.bookstore.dto.CategoryRequestDto;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto categoryRequestDto);

    CategoryDto update(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);
}
