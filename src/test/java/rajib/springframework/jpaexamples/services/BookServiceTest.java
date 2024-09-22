package rajib.springframework.jpaexamples.services;

import static org.junit.jupiter.api.Assertions.*;

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
import rajib.springframework.jpaexamples.entities.Book;
import rajib.springframework.jpaexamples.repositories.AuthorRepository;
import rajib.springframework.jpaexamples.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testCreateBook() {
        // Arrange
        Author author = new Author("Author Name");
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        Book book = new Book("Book Title", author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book createdBook = bookService.createBook("Book Title", 1L);

        // Assert
        assertNotNull(createdBook);
        assertEquals("Book Title", createdBook.getTitle());
        assertEquals("Author Name", createdBook.getAuthor().getName());
        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testCreateBook_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookService.createBook("Book Title", 1L));
        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void testGetAllBooks() {
        // Arrange
        List<Book> books = Arrays.asList(new Book("Book1", null), new Book("Book2", null));
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Book1", result.get(0).getTitle());
        assertEquals("Book2", result.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById_ExistingId() {
        // Arrange
        Book book = new Book("Book Title", null);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Book Title", result.get().getTitle());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetBookById_NonExistingId() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetBooksByAuthorName_ExistingAuthorName() {
        // Arrange
        List<Book> books = Arrays.asList(new Book("Book1", null), new Book("Book2", null));
        when(bookRepository.findByAuthorName(anyString())).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooksByAuthorName("Author Name");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Book1", result.get(0).getTitle());
        assertEquals("Book2", result.get(1).getTitle());
        verify(bookRepository, times(1)).findByAuthorName(anyString());
    }

    @Test
    public void testGetBooksByAuthorName_NonExistingAuthorName() {
        // Arrange
        when(bookRepository.findByAuthorName(anyString())).thenReturn(Arrays.asList());

        // Act
        List<Book> result = bookService.getBooksByAuthorName("NonExistingAuthor");

        // Assert
        assertEquals(0, result.size());
        verify(bookRepository, times(1)).findByAuthorName(anyString());
    }

    @Test
    public void testUpdateBook_ExistingId() {
        // Arrange
        Book existingBook = new Book("Old Title", null);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        // Act
        Book updatedBook = bookService.updateBook(1L, "New Title");

        // Assert
        assertNotNull(updatedBook);
        assertEquals("New Title", updatedBook.getTitle());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBook_NonExistingId() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bookService.updateBook(1L, "New Title"));
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void testDeleteBook_ExistingId() {
        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteBook_NonExistingId() {
        // Act
        bookService.deleteBook(99L);

        // Assert
        verify(bookRepository, times(1)).deleteById(anyLong());
    }
}
