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
import spring.bookstore.mapper.CategoryMapper;
import spring.bookstore.model.Category;
import spring.bookstore.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("getById — returns DTO")
    void getById_ok() {
        Category entity = new Category();
        entity.setId(1L);
        entity.setName("Drama");

        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Drama");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapper.toDto(entity)).thenReturn(expected);

        CategoryDto actual = categoryService.getById(1L);

        assertEquals(expected, actual);
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("deleteById(): deleted Category when exist")
    void deleteById_ok() {

        when(categoryRepository.existsById(1L))
                .thenReturn(true);

        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }
    @Test
    @DisplayName("createCategory — saves and returns DTO")
    void create_ok() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fantasy");

        Category entity = new Category();
        entity.setName("Fantasy");

        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Fantasy");

        when(categoryMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(entity);
        when(categoryMapper.toDto(entity)).thenReturn(expected);

        CategoryDto actual = categoryService.save(requestDto);

        assertEquals(expected, actual);
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(entity);
        verify(categoryMapper).toDto(entity);
    }
}
