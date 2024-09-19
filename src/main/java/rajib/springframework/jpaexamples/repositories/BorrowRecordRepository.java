package rajib.springframework.jpaexamples.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rajib.springframework.jpaexamples.entities.BorrowRecord;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
}
