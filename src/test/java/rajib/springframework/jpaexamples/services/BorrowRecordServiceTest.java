package rajib.springframework.jpaexamples.services;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rajib.springframework.jpaexamples.entities.Book;
import rajib.springframework.jpaexamples.entities.BorrowRecord;
import rajib.springframework.jpaexamples.entities.User;
import rajib.springframework.jpaexamples.repositories.BookRepository;
import rajib.springframework.jpaexamples.repositories.BorrowRecordRepository;
import rajib.springframework.jpaexamples.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BorrowRecordServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowRecordService borrowRecordService;

    @Test
    public void testCreateBorrowRecord() {
        // Arrange
        Book book = new Book("Book Title", null);
        User user = new User("User Name", "user@example.com");
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        BorrowRecord borrowRecord = new BorrowRecord(book, user, LocalDateTime.now());
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);

        // Act
        BorrowRecord createdRecord = borrowRecordService.createBorrowRecord(1L, 1L, LocalDateTime.now());

        // Assert
        assertNotNull(createdRecord);
        assertEquals("Book Title", createdRecord.getBook().getTitle());
        assertEquals("User Name", createdRecord.getUser().getName());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(borrowRecordRepository, times(1)).save(any(BorrowRecord.class));
    }

    @Test
    public void testCreateBorrowRecord_BookNotFound() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> borrowRecordService.createBorrowRecord(1L, 1L, LocalDateTime.now()));
        verify(bookRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).findById(anyLong());
        verify(borrowRecordRepository, times(0)).save(any(BorrowRecord.class));
    }

    @Test
    public void testCreateBorrowRecord_UserNotFound() {
        // Arrange
        Book book = new Book("Book Title", null);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> borrowRecordService.createBorrowRecord(1L, 1L, LocalDateTime.now()));
        verify(bookRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(borrowRecordRepository, times(0)).save(any(BorrowRecord.class));
    }

    @Test
    public void testGetAllBorrowRecords() {
        // Arrange
        List<BorrowRecord> borrowRecords = Arrays.asList(new BorrowRecord(null, null, LocalDateTime.now()));
        when(borrowRecordRepository.findAll()).thenReturn(borrowRecords);

        // Act
        List<BorrowRecord> result = borrowRecordService.getAllBorrowRecords();

        // Assert
        assertEquals(1, result.size());
        verify(borrowRecordRepository, times(1)).findAll();
    }

    @Test
    public void testGetBorrowRecordById_ExistingId() {
        // Arrange
        BorrowRecord borrowRecord = new BorrowRecord(null, null, LocalDateTime.now());
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.of(borrowRecord));

        // Act
        Optional<BorrowRecord> result = borrowRecordService.getBorrowRecordById(1L);

        // Assert
        assertTrue(result.isPresent());
        verify(borrowRecordRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetBorrowRecordById_NonExistingId() {
        // Arrange
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<BorrowRecord> result = borrowRecordService.getBorrowRecordById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(borrowRecordRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateBorrowRecord_ExistingId() {
        // Arrange
        BorrowRecord existingRecord = new BorrowRecord(null, null, LocalDateTime.now());
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.of(existingRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(existingRecord);

        // Act
        LocalDateTime newReturnDate = LocalDateTime.now().plusDays(1);
        BorrowRecord updatedRecord = borrowRecordService.updateBorrowRecord(1L, newReturnDate);

        // Assert
        assertNotNull(updatedRecord);

        // Compare without nanoseconds
        assertEquals(newReturnDate.withNano(0), updatedRecord.getReturnDate().withNano(0));

        // Alternatively, compare only date and time up to seconds
        assertEquals(newReturnDate.truncatedTo(ChronoUnit.SECONDS),
                updatedRecord.getReturnDate().truncatedTo(ChronoUnit.SECONDS));

        verify(borrowRecordRepository, times(1)).findById(anyLong());
        verify(borrowRecordRepository, times(1)).save(any(BorrowRecord.class));
    }


    @Test
    public void testUpdateBorrowRecord_NonExistingId() {
        // Arrange
        when(borrowRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> borrowRecordService.updateBorrowRecord(1L, LocalDateTime.now().plusDays(1)));
        verify(borrowRecordRepository, times(1)).findById(anyLong());
        verify(borrowRecordRepository, times(0)).save(any(BorrowRecord.class));
    }

    @Test
    public void testDeleteBorrowRecord_ExistingId() {
        // Act
        borrowRecordService.deleteBorrowRecord(1L);

        // Assert
        verify(borrowRecordRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteBorrowRecord_NonExistingId() {
        // Act
        borrowRecordService.deleteBorrowRecord(99L);

        // Assert
        verify(borrowRecordRepository, times(1)).deleteById(anyLong());
    }
}
