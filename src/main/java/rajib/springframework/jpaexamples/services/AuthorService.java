package rajib.springframework.jpaexamples.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rajib.springframework.jpaexamples.entities.Author;
import rajib.springframework.jpaexamples.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(String authorName) {
        Author author = new Author(authorName);
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Optional<Author> getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    public Author updateAuthor(Long id, String newName) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(newName);
                    return authorRepository.save(author);
                }).orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}


