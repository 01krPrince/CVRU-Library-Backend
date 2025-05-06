package BLMS.repository;

import BLMS.model.BorrowRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends MongoRepository<BorrowRecord, String> {
    List<BorrowRecord> findByStudentId(String studentId);
    List<BorrowRecord> findByBookId(String bookId);
}