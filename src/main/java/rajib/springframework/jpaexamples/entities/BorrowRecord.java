package rajib.springframework.jpaexamples.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime borrowDate;

    private LocalDateTime returnDate;
    public BorrowRecord(Book book, User user, LocalDateTime borrowDate) {
        this.book = book;
        this.user = user;
        this.borrowDate = borrowDate;
    }

}

