package rajib.springframework.jpaexamples.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rajib.springframework.jpaexamples.entities.Author;
import rajib.springframework.jpaexamples.entities.Book;
import rajib.springframework.jpaexamples.repositories.AuthorRepository;
import rajib.springframework.jpaexamples.repositories.BookRepository;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book addBook(String title, String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElse(new Author(authorName));
        Book book = new Book(title, author);
        return bookRepository.save(book);
    }
}