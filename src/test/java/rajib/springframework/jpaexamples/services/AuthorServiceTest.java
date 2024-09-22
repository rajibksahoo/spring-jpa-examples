package rajib.springframework.jpaexamples.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rajib.springframework.jpaexamples.entities.Author;
import rajib.springframework.jpaexamples.repositories.AuthorRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    public void testCreateAuthor() {
        // Arrange
        Author author = new Author("John Doe");
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // Act
        Author createdAuthor = authorService.createAuthor("John Doe");

        // Assert
        assertNotNull(createdAuthor);
        assertEquals("John Doe", createdAuthor.getName());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testGetAllAuthors() {
        // Arrange
        List<Author> authors = Arrays.asList(new Author("Author1"), new Author("Author2"));
        when(authorRepository.findAll()).thenReturn(authors);

        // Act
        List<Author> result = authorService.getAllAuthors();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Author1", result.get(0).getName());
        assertEquals("Author2", result.get(1).getName());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void testGetAuthorById_ExistingId() {
        // Arrange
        Author author = new Author("Author1");
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        // Act
        Optional<Author> result = authorService.getAuthorById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Author1", result.get().getName());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAuthorById_NonExistingId() {
        // Arrange
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Author> result = authorService.getAuthorById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAuthorByName_ExistingName() {
        // Arrange
        Author author = new Author("Author1");
        when(authorRepository.findByName(anyString())).thenReturn(Optional.of(author));

        // Act
        Optional<Author> result = authorService.getAuthorByName("Author1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Author1", result.get().getName());
        verify(authorRepository, times(1)).findByName(anyString());
    }

    @Test
    public void testGetAuthorByName_NonExistingName() {
        // Arrange
        when(authorRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Author> result = authorService.getAuthorByName("NonExistingName");

        // Assert
        assertFalse(result.isPresent());
        verify(authorRepository, times(1)).findByName(anyString());
    }

    @Test
    public void testUpdateAuthor_ExistingId() {
        // Arrange
        Author existingAuthor = new Author("Old Name");
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        // Act
        Author updatedAuthor = authorService.updateAuthor(1L, "New Name");

        // Assert
        assertNotNull(updatedAuthor);
        assertEquals("New Name", updatedAuthor.getName());
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testUpdateAuthor_NonExistingId() {
        // Arrange
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authorService.updateAuthor(1L, "New Name"));
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorRepository, times(0)).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor_ExistingId() {
        // Act
        authorService.deleteAuthor(1L);

        // Assert
        verify(authorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteAuthor_NonExistingId() {
        // Act
        authorService.deleteAuthor(99L);

        // Assert
        verify(authorRepository, times(1)).deleteById(anyLong());
    }
}
