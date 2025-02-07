package spring.book_store;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.book_store.model.Book;
import spring.book_store.service.BookService;

@SpringBootApplication
public class BookStoreApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Кобзар");
            book.setAuthor("Т.Г.Шевченко");
            book.setIsbn("123");
            book.setPrice(BigDecimal.valueOf(99));
            book.setDescription("Книга Кобзар");
            bookService.save(book);

            bookService.findAll().forEach(System.out::println);
        };
    }
}
