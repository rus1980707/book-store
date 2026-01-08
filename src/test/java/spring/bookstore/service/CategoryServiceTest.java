package spring.bookstore.service;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.bookstore.dto.CategoryDto;
import spring.bookstore.dto.CategoryRequestDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.mapper.CategoryMapper;
import spring.bookstore.model.Category;
import spring.bookstore.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("getById(): return CategoryDto when Category exist")
    void getById_existingCategory_returnsDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Drama");

        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Drama");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category))
                .thenReturn(dto);

        CategoryDto result = categoryService.getById(1L);

        assertEquals("Drama", result.getName());
    }

    @Test
    @DisplayName("getById(): throw EntityNotFoundException when Category not exist")
    void getById_notExistingCategory_throwsException() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(1L));
    }

    @Test
    @DisplayName("deleteById(): deleted Category when exist")
    void deleteById_existingCategory_deletes() {

        when(categoryRepository.existsById(1L))
                .thenReturn(true);

        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }
    @Test
    @DisplayName("createCategory — saves and returns DTO")
    void create_ok() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        Category entity = new Category();
        entity.setName("Sci-Fi");
        CategoryDto dto = new CategoryDto();

        when(categoryMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(entity);
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        CategoryDto result = categoryService.save(requestDto);

    }
}
