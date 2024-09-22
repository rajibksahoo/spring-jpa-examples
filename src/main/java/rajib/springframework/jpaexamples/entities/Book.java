package rajib.springframework.jpaexamples.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private  String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private  Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowRecord> borrowRecords;


    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }
}



