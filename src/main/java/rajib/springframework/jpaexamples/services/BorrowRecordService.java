package rajib.springframework.jpaexamples.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rajib.springframework.jpaexamples.entities.Book;
import rajib.springframework.jpaexamples.entities.BorrowRecord;
import rajib.springframework.jpaexamples.entities.User;
import rajib.springframework.jpaexamples.repositories.BookRepository;
import rajib.springframework.jpaexamples.repositories.BorrowRecordRepository;
import rajib.springframework.jpaexamples.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowRecord createBorrowRecord(Long bookId, Long userId, LocalDateTime borrowDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BorrowRecord borrowRecord = new BorrowRecord(book, user, borrowDate);
        return borrowRecordRepository.save(borrowRecord);
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public Optional<BorrowRecord> getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id);
    }

    public BorrowRecord updateBorrowRecord(Long id, LocalDateTime returnDate) {
        return borrowRecordRepository.findById(id)
                .map(borrowRecord -> {
                    borrowRecord.setReturnDate(returnDate);
                    return borrowRecordRepository.save(borrowRecord);
                }).orElseThrow(() -> new RuntimeException("BorrowRecord not found"));
    }

    public void deleteBorrowRecord(Long id) {
        borrowRecordRepository.deleteById(id);
    }
}
