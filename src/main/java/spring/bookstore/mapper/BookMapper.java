package spring.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import spring.bookstore.dto.BookDto;
import spring.bookstore.dto.BookDtoWithoutCategoryIds;
import spring.bookstore.dto.CreateBookRequestDto;
import spring.bookstore.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    Book toEntity(CreateBookRequestDto dto);

    void updateBookDto(CreateBookRequestDto bookDto,
                       @MappingTarget Book currentBook);

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
