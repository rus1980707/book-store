package spring.bookstore.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.bookstore.dto.BookDto;
import spring.bookstore.dto.CreateBookRequestDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.mapper.BookMapper;
import spring.bookstore.model.Book;
import spring.bookstore.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new
                        EntityNotFoundException("Book by id not found")));
    }

    @Override
    public BookDto createBook(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookDto) {
        Book currentBook = bookRepository.findById(id)
                .orElseThrow(() -> new
                        EntityNotFoundException("Book by id not found"));
        bookMapper.updateBookDto(bookDto, currentBook);

        bookRepository.save(currentBook);
        return bookMapper.toDto(currentBook);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
