package rajib.springframework.jpaexamples.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rajib.springframework.jpaexamples.entities.Author;
import rajib.springframework.jpaexamples.entities.Book;
import rajib.springframework.jpaexamples.repositories.AuthorRepository;
import rajib.springframework.jpaexamples.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    public Book addBook(String title, String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElse(new Author(authorName));
        Book book = new Book(title, author);
        return bookRepository.save(book);
    }

    public Book createBook(String title, Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        Book book = new Book(title, author);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    public Book updateBook(Long id, String newTitle) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newTitle);
                    return bookRepository.save(book);
                }).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}