package rajib.springframework.jpaexamples.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rajib.springframework.jpaexamples.entities.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

}
