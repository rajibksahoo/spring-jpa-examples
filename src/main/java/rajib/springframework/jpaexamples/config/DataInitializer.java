package rajib.springframework.jpaexamples.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rajib.springframework.jpaexamples.entities.Author;
import rajib.springframework.jpaexamples.entities.Book;
import rajib.springframework.jpaexamples.entities.BorrowRecord;
import rajib.springframework.jpaexamples.entities.User;
import rajib.springframework.jpaexamples.repositories.AuthorRepository;
import rajib.springframework.jpaexamples.repositories.BookRepository;
import rajib.springframework.jpaexamples.repositories.BorrowRecordRepository;
import rajib.springframework.jpaexamples.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            UserRepository userRepository,
            BorrowRecordRepository borrowRecordRepository) {
        return args -> {

            // Create Authors
            Author author1 = new Author("J.K. Rowling");
            Author author2 = new Author("George R.R. Martin");

            authorRepository.saveAll(Arrays.asList(author1, author2));

            // Create Books
            Book book1 = new Book("Harry Potter and the Sorcerer's Stone", author1);
            Book book2 = new Book("A Game of Thrones", author2);

            bookRepository.saveAll(Arrays.asList(book1, book2));

            // Create Users
            User user1 = new User("Alice", "alice@example.com");
            User user2 = new User("Bob", "bob@example.com");

            userRepository.saveAll(Arrays.asList(user1, user2));

            // Create Borrow Records
            BorrowRecord record1 = new BorrowRecord(book1, user1, LocalDateTime.now().minusDays(10));
            record1.setReturnDate(LocalDateTime.now().minusDays(5));  // Book returned

            BorrowRecord record2 = new BorrowRecord(book2, user2, LocalDateTime.now().minusDays(2));
            // Book not yet returned

            borrowRecordRepository.saveAll(Arrays.asList(record1, record2));

            System.out.println("Data initialization complete.");
        };
    }
}
