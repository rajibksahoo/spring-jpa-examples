package rajib.springframework.jpaexamples.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import rajib.springframework.jpaexamples.entities.Author;
import rajib.springframework.jpaexamples.entities.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorName(String name);
}

